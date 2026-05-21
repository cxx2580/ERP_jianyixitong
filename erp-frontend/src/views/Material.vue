<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span>物料管理</span>
        <el-button class="float-right" type="primary" size="small" @click="handleAdd">新增物料</el-button>
      </div>
      <div style="margin-bottom:20px">
        <el-input v-model="keyword" placeholder="搜索物料" style="width:300px;margin-right:10px" @keyup.enter.native="load"></el-input>
        <el-button type="primary" @click="load">搜索</el-button>
        <el-button type="success" style="margin-left:10px" @click="showBom">BOM 物料清单</el-button>
      </div>
      <el-table :data="tableData" stripe style="width:100%">
        <el-table-column prop="materialNo" label="物料编号" width="120"></el-table-column>
        <el-table-column prop="materialName" label="物料名称" width="160"></el-table-column>
        <el-table-column prop="specification" label="规格" width="140"></el-table-column>
        <el-table-column prop="unit" label="单位" width="80"></el-table-column>
        <el-table-column prop="price" label="单价" width="100">
          <template slot-scope="s">¥{{ s.row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="s">
            <el-tag :type="s.row.status===1?'success':'info'">{{ s.row.status===1?'启用':'禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="s">
            <el-button size="mini" @click="handleEdit(s.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(s.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;text-align:right" @current-change="onPage" :current-page="page" :page-size="pageSize" layout="total,prev,pager,next" :total="total"></el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="物料编号" prop="materialNo">
          <el-input v-model="form.materialNo" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName"></el-input>
        </el-form-item>
        <el-form-item label="规格"><el-input v-model="form.specification"></el-input></el-form-item>
        <el-form-item label="单位"><el-input v-model="form.unit"></el-input></el-form-item>
        <el-form-item label="单价"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%"></el-input-number></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" style="width:100%"></el-input-number></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></span>
    </el-dialog>

    <el-dialog title="BOM 物料清单管理" :visible.sync="bomVisible" width="700px">
      <el-form label-width="100px">
        <el-form-item label="选择产品">
          <el-select v-model="bomProductId" filterable placeholder="请选择产品" @change="loadBom" style="width:100%">
            <el-option v-for="p in products" :key="p.id" :label="p.productNo + ' ' + p.productName" :value="p.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="bomItems" stripe size="small" style="width:100%">
        <el-table-column label="物料" width="280">
          <template slot-scope="s">
            <el-select v-model="s.row.materialId" filterable placeholder="选择物料" style="width:100%" size="small">
              <el-option v-for="m in allMaterials" :key="m.id" :label="m.materialNo + ' ' + m.materialName" :value="m.id"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="用量" width="120">
          <template slot-scope="s">
            <el-input-number v-model="s.row.quantity" :min="1" size="small" controls-position="right"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="s">
            <el-button type="danger" size="mini" @click="bomItems.splice(s.$index,1)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:12px"><el-button type="primary" size="small" @click="bomItems.push({materialId:null,quantity:1})">添加物料</el-button></div>
      <span slot="footer"><el-button @click="bomVisible=false">取消</el-button><el-button type="primary" @click="saveBom">保存BOM</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Material',
  data() {
    return {
      tableData:[], keyword:'', page:1, pageSize:10, total:0, dialogVisible:false, dialogTitle:'', isEdit:false,
      form:{ id:null, materialNo:'', materialName:'', specification:'', unit:'', price:0, stock:0, status:1 },
      rules:{ materialNo:[{required:true,message:'请输入物料编号'}], materialName:[{required:true,message:'请输入物料名称'}] },
      bomVisible:false, bomProductId:null, bomItems:[], products:[], allMaterials:[]
    }
  },
  mounted() { this.load(); this.loadProducts(); this.loadAllMaterials() },
  methods: {
    load() {
      this.$axios.get('/api/material/list',{params:{keyword:this.keyword,page:this.page,pageSize:this.pageSize}}).then(res=>{
        if(res.data.code===200){ this.tableData=res.data.data.list; this.total=res.data.data.total }
      })
    },
    loadProducts() { this.$axios.get('/api/product/list',{params:{pageSize:999}}).then(res=>{ if(res.data.code===200) this.products=res.data.data.list }) },
    loadAllMaterials() { this.$axios.get('/api/material/list',{params:{pageSize:999}}).then(res=>{ if(res.data.code===200) this.allMaterials=res.data.data.list }) },
    onPage(p) { this.page=p; this.load() },
    handleAdd() { this.isEdit=false; this.dialogTitle='新增物料'; this.form={ id:null, materialNo:'M'+Date.now(), materialName:'', specification:'', unit:'', price:0, stock:0, status:1 }; this.dialogVisible=true },
    handleEdit(row) { this.isEdit=true; this.dialogTitle='编辑物料'; this.form={...row}; this.dialogVisible=true },
    handleSubmit() {
      this.$refs.form.validate(v=>{
        if(v){
          const url=this.isEdit?'/api/material/update':'/api/material/add'
          this.$axios.post(url,this.form).then(res=>{
            if(res.data.code===200){ this.$message.success('操作成功'); this.dialogVisible=false; this.load() } else this.$message.error(res.data.message)
          })
        }
      })
    },
    handleDelete(row) { this.$confirm('确定删除？','提示',{type:'warning'}).then(()=>{ this.$axios.delete('/api/material/delete/'+row.id).then(res=>{ if(res.data.code===200){ this.$message.success('删除成功'); this.load() } }) }).catch(()=>{}) },
    showBom() { this.bomProductId=null; this.bomItems=[]; this.bomVisible=true },
    loadBom(pid) {
      this.$axios.get('/api/material/bom/'+pid).then(res=>{
        if(res.data.code===200) this.bomItems = (res.data.data||[]).map(b=>({materialId:b.materialId,quantity:b.quantity,id:b.id}))
      })
    },
    saveBom() {
      if(!this.bomProductId){ this.$message.warning('请先选择产品'); return }
      const validItems = this.bomItems.filter(b => b.materialId)
      if(validItems.length === 0 && this.bomItems.length > 0){ this.$message.warning('请为物料行选择具体物料,或删除空行'); return }
      this.$axios.post('/api/material/bom/save',{ productId:this.bomProductId, boms:validItems }).then(res=>{
        if(res.data.code===200){ this.$message.success('BOM保存成功'); this.bomVisible=false }
      })
    }
  }
}
</script>
