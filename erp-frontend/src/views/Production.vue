<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span>生产管理</span>
        <el-button class="float-right" type="primary" size="small" @click="handleAdd">新增生产订单</el-button>
      </div>
      <div style="margin-bottom:20px">
        <el-input v-model="searchNo" placeholder="搜索生产单号" style="width:300px;margin-right:10px"></el-input>
        <el-button type="primary" @click="searchOrders">搜索</el-button>
      </div>
      <el-table :data="tableData" stripe style="width:100%">
        <el-table-column prop="productionNo" label="生产单号" width="150"></el-table-column>
        <el-table-column prop="productName" label="产品名称" width="150"></el-table-column>
        <el-table-column prop="specification" label="规格" width="130"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120"><template slot-scope="s">¥{{ s.row.totalAmount }}</template></el-table-column>
        <el-table-column prop="salesOrderNo" label="关联销售单" width="160">
          <template slot-scope="s"><span v-if="s.row.salesOrderNo" style="color:#4F6EF7;cursor:pointer" @click="$router.push('/sales-order')">{{ s.row.salesOrderNo }}</span><span v-else style="color:#ccc">--</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="s"><el-tag :type="getStatusType(s.row.status)">{{ getStatusText(s.row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="100"></el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="s">
            <el-button size="mini" @click="handleEdit(s.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(s.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;text-align:right" @current-change="handlePageChange" :current-page="page" :page-size="pageSize" layout="total,prev,pager,next" :total="total"></el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="850px" @opened="onDialogOpened">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="生产单号" prop="productionNo"><el-input v-model="form.productionNo" :disabled="isEdit"></el-input></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="产品" prop="productId"><el-select v-model="form.productId" placeholder="选择产品" style="width:100%" @change="onProductChange" :disabled="isEdit" filterable><el-option v-for="p in products" :key="p.id" :label="p.productName" :value="p.id"></el-option></el-select></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="关联销售单"><el-input :value="form.salesOrderNo || '无'" disabled size="small"></el-input></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="规格"><el-input v-model="form.specification" disabled></el-input></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="单位"><el-input v-model="form.unit" disabled></el-input></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="单价"><el-input v-model="form.price" disabled></el-input></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产数量" prop="quantity"><el-input-number v-model="form.quantity" :min="1" style="width:100%" @change="calcTotalAmount"></el-input-number></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总金额"><span style="font-size:18px;font-weight:700;background:linear-gradient(135deg,#e8836f,#d4526e);-webkit-background-clip:text;-webkit-text-fill-color:transparent">¥{{ form.totalAmount }}</span></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="预计完成" prop="endDate"><el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="实际完成"><el-date-picker v-model="form.actualEndDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio :label="0">计划中</el-radio><el-radio :label="1">生产中</el-radio><el-radio :label="2">已完成</el-radio><el-radio :label="3">已取消</el-radio></el-radio-group></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人" prop="responsiblePerson"><el-input v-model="form.responsiblePerson" placeholder="负责人"></el-input></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="2"></el-input></el-form-item>

        <el-divider content-position="left">生产物料清单</el-divider>
        <div style="margin-bottom:8px">
          <el-button type="success" size="small" @click="loadBomToMaterials" :disabled="!form.productId">从BOM加载物料</el-button>
          <el-button type="primary" size="small" @click="addMaterialRow">手动添加物料</el-button>
        </div>
        <el-table :data="materialItems" stripe size="small" style="width:100%">
          <el-table-column label="物料" width="280">
            <template slot-scope="s">
              <el-select v-model="s.row.materialId" filterable placeholder="选择物料" style="width:100%" size="small" @change="onMaterialChange(s.$index)">
                <el-option v-for="m in allMaterials" :key="m.id" :label="m.materialNo + ' ' + m.materialName" :value="m.id"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80"><template slot-scope="s">{{ s.row.unit }}</template></el-table-column>
          <el-table-column label="单价" width="100"><template slot-scope="s">¥{{ s.row.price }}</template></el-table-column>
          <el-table-column label="用量" width="150">
            <template slot-scope="s"><el-input-number v-model="s.row.quantity" :min="1" size="small" controls-position="right" @change="calcMaterialSubtotal(s.$index)"></el-input-number></template>
          </el-table-column>
          <el-table-column label="小计" width="100"><template slot-scope="s">¥{{ s.row.subtotal }}</template></el-table-column>
          <el-table-column label="操作" width="80"><template slot-scope="s"><el-button type="danger" size="mini" @click="materialItems.splice(s.$index,1)">删除</el-button></template></el-table-column>
        </el-table>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Production',
  data() {
    return {
      searchNo:'', tableData:[], page:1, pageSize:10, total:0, products:[], allMaterials:[],
      dialogVisible:false, dialogTitle:'', isEdit:false,
      form:{ id:null, productionNo:'', productId:null, productName:'', specification:'', unit:'', quantity:1, price:0, totalAmount:0, startDate:'', endDate:'', actualEndDate:'', status:0, responsiblePerson:'', remark:'', salesOrderId:null, salesOrderNo:'' },
      materialItems:[],
      rules:{ productionNo:[{required:true,message:'请输入生产单号'}], productId:[{required:true,message:'请选择产品'}], quantity:[{required:true,message:'请输入数量'}] }
    }
  },
  mounted() {
    this.loadOrders(); this.loadProducts()
    this.$axios.get('/api/material/list',{params:{pageSize:999}}).then(res=>{ if(res.data.code===200) this.allMaterials=res.data.data.list })
    const { salesOrderId, salesOrderNo, productId } = this.$route.query
    if (salesOrderId) {
      this.handleAdd()
      this.form.salesOrderId = Number(salesOrderId)
      this.form.salesOrderNo = salesOrderNo || ''
      if (productId) {
        this.form.productId = Number(productId)
        this.onProductChange(this.form.productId)
      }
    }
  },
  methods: {
    loadOrders() { this.$axios.get('/api/production-order/list',{params:{productionNo:this.searchNo,page:this.page,pageSize:this.pageSize}}).then(res=>{ if(res.data.code===200){ this.tableData=res.data.data.list; this.total=res.data.data.total } }) },
    searchOrders() { this.page=1; this.loadOrders() },
    handlePageChange(p) { this.page=p; this.loadOrders() },
    loadProducts() { this.$axios.get('/api/product/list',{params:{pageSize:999}}).then(res=>{ if(res.data.code===200) this.products=res.data.data.list }) },
    getStatusType(s) { return ['info','warning','success','danger'][s]||'info' },
    getStatusText(s) { return ['计划中','生产中','已完成','已取消'][s]||'未知' },
    handleAdd() {
      this.isEdit=false; this.dialogTitle='新增生产订单'
      this.form={ id:null, productionNo:'PRD'+Date.now(), productId:null, productName:'', specification:'', unit:'', quantity:1, price:0, totalAmount:0, startDate:'', endDate:'', actualEndDate:'', status:0, responsiblePerson:'', remark:'', salesOrderId:null, salesOrderNo:'' }
      this.materialItems=[]
      this.dialogVisible=true
    },
    onDialogOpened() { if(this.$refs.form) this.$refs.form.clearValidate() },
    handleEdit(row) {
      this.isEdit=true; this.dialogTitle='编辑生产订单'
      this.$axios.get('/api/production-order/get/'+row.id).then(res=>{
        if(res.data.code===200){
          this.form={...res.data.data.order}
          this.materialItems=(res.data.data.materials||[]).map(m=>({...m}))
          this.dialogVisible=true
        }
      })
    },
    onProductChange(pid) {
      const p=this.products.find(pp=>pp.id===pid)
      if(p){ this.form.productName=p.productName; this.form.specification=p.specification; this.form.unit=p.unit; this.form.price=p.price; this.calcTotalAmount() }
    },
    calcTotalAmount() { this.form.totalAmount=(this.form.price||0)*(this.form.quantity||0) },
    onMaterialChange(idx) {
      const m=this.allMaterials.find(mm=>mm.id===this.materialItems[idx].materialId)
      if(m){ this.materialItems[idx].materialName=m.materialName; this.materialItems[idx].specification=m.specification; this.materialItems[idx].unit=m.unit; this.materialItems[idx].price=m.price; this.calcMaterialSubtotal(idx) }
    },
    calcMaterialSubtotal(idx) {
      const item=this.materialItems[idx]; item.subtotal=(item.price||0)*(item.quantity||0)
    },
    loadBomToMaterials() {
      this.$axios.get('/api/material/bom/'+this.form.productId).then(res=>{
        if(res.data.code===200 && res.data.data){
          this.materialItems=res.data.data.map(b=>({materialId:b.materialId,materialName:b.materialName,specification:b.specification,unit:b.unit,price:b.price,quantity:b.quantity,subtotal:b.price*b.quantity}))
          this.$message.success('已从BOM加载物料清单')
        } else { this.$message.warning('该产品未配置BOM，请手动添加物料') }
      })
    },
    addMaterialRow() { this.materialItems.push({materialId:null,materialName:'',specification:'',unit:'',price:0,quantity:1,subtotal:0}) },
    handleSubmit() {
      this.$refs.form.validate(v=>{
        if(v){
          const url=this.isEdit?'/api/production-order/update':'/api/production-order/add'
          this.$axios.post(url,{order:this.form,materials:this.materialItems}).then(res=>{
            if(res.data.code===200){ this.$message.success('操作成功'); this.dialogVisible=false; this.loadOrders() } else this.$message.error(res.data.message)
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除？','提示',{type:'warning'}).then(()=>{ this.$axios.delete('/api/production-order/delete/'+row.id).then(res=>{ if(res.data.code===200){ this.$message.success('删除成功'); this.loadOrders() } }) }).catch(()=>{})
    }
  }
}
</script>
