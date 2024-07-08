<template>
  <el-container style="justify-content: center; align-items: center;">
    <el-card class="box-card" style="width: 400px;">
      <div slot="header" class="clearfix">
        <span>登录</span>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="120px">
        <el-form-item label="用户名/Email" prop="name">
          <el-input v-model="loginForm.name"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </el-container>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router'
import { useSessionStore } from '@/stores/session'
import * as sessionApi from '@/services/session'

const router = useRouter()
const sessionStore = useSessionStore()

const loginForm = reactive({
  name: '',
  password: '',
  rememberMe: false
});

const rules = {
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
};

const loginFormRef = ref(null);

const handleLogin = async () => {
  await loginFormRef.value.validate(async(valid) => {
    if (valid) {
      const resp = await sessionApi.signIn(loginForm.name, loginForm.password);

      ElMessage.success('登录成功');
      sessionStore.signIn(resp, loginForm.rememberMe);

      router.push({ name: 'MessageBoard' })
    } else {
      ElMessage.error('请检查表单填写是否正确');
    }
  });
};
</script>

<style scoped>
.box-card {
  margin-top: 50px;
}
</style>
