<template>
  <el-dialog v-model="visible" title="添加基金持仓" width="500px">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="基金代码" prop="fundCode">
        <el-input v-model="form.fundCode" placeholder="请输入基金代码"></el-input>
      </el-form-item>
      <el-form-item label="买入价格" prop="buyPrice">
        <el-input-number v-model="form.buyPrice" :precision="4" :step="0.0001" :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="份额" prop="shares">
        <el-input-number v-model="form.shares" :precision="2" :step="100" :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="买入日期" prop="buyDate">
        <el-date-picker v-model="form.buyDate" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { addHolding } from '../api/fund'

const visible = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({
  fundCode: '',
  buyPrice: 0,
  shares: 0,
  buyDate: new Date()
})

const rules = reactive<FormRules>({
  fundCode: [
    { required: true, message: '请输入基金代码', trigger: 'blur' }
  ],
  buyPrice: [
    { required: true, message: '请输入买入价格', trigger: 'blur' }
  ],
  shares: [
    { required: true, message: '请输入份额', trigger: 'blur' }
  ],
  buyDate: [
    { required: true, message: '请选择买入日期', trigger: 'change' }
  ]
})

const emit = defineEmits<{
  success: []
}>()

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const formattedDate = typeof form.buyDate === 'string' ? form.buyDate :
          form.buyDate instanceof Date ? form.buyDate.toISOString().split('T')[0] : ''

        await addHolding({
          fundCode: form.fundCode,
          buyPrice: form.buyPrice,
          shares: form.shares,
          buyDate: formattedDate
        })

        ElMessage.success('添加成功')
        visible.value = false
        emit('success')

        // 重置表单
        form.fundCode = ''
        form.buyPrice = 0
        form.shares = 0
        form.buyDate = new Date()
      } catch (error) {
        ElMessage.error('添加失败')
      }
    }
  })
}

const show = () => {
  visible.value = true
}

defineExpose({
  show
})
</script>