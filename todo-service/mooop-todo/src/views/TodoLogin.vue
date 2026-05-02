<script setup>
import { getCurrentInstance, reactive , ref} from 'vue';
import { useRouter } from 'vue-router'
import { userAuthStore } from '../store/auth'
import { ApiResultCode , ApiUrlCode } from '../code/ApiCode'
import { AppCode } from '../code/AppCodes';

import MemberForm from '../components/MemberForm.vue'


const router = useRouter()
const { proxy } = getCurrentInstance()
const authStore = userAuthStore()

const loginForm = reactive({
    "userId":""
    ,"password":""
})

const isMemberFormOpen = ref(false)
const modeType=ref('REG')


const login = async ()=>{
    try{
        const signRes = await proxy.$api.post( ApiUrlCode.SIGNIN , {"userId":loginForm.userId , "password":loginForm.password});
        console.dir(signRes)
        if(signRes.data.result === ApiResultCode.FAIL){
            alert(signRes.data.message);
        }else{
            localStorage.setItem(AppCode.AUTH_TOKEN , signRes.data.body)
            // authStore.setToken(signRes.data.body)
            router.push({name:"main"})
        }
    }catch(err){
        console.dir(err)
        alert('오류가 발생했습니다.')

    }
}
</script>
<template>
    <div class="login-container">

    <div class="login-card">

        <!-- 타이틀 -->
        <div class="login-title">
            MOoop Todo
        </div>

        <!-- 아이디 -->
        <div class="form-group">
            <input type="text" placeholder="아이디" v-model="loginForm.userId">
        </div>

        <!-- 암호 -->
        <div class="form-group">
            <input type="password" placeholder="암호" v-model="loginForm.password">
        </div>

        <!-- 로그인 버튼 -->
        <button class="login-btn" v-on:click="login">
            로그인
        </button>

        <!-- 회원가입 링크 -->
        <div class="signup-link">
            계정이 없으신가요?
            <a href="javascript:void(0)" @click="isMemberFormOpen = true">회원가입</a>
             <MemberForm :mode="modeType" v-if="isMemberFormOpen" @close="isMemberFormOpen = false" />
        </div>

    </div>

</div>
</template>
<style scoped>
/* 기본 */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

/* 배경 */
body {
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;

    background: linear-gradient(
        135deg,
        #eef2f7,
        #e3f2fd
    );
}

/* 컨테이너 */
.login-container {
    width: 100%;
    padding: 16px;
}

/* 카드 (width 확장) */
.login-card {

    max-width: 420px;   /* 기존 360 → 확장 */
    width: 100%;

    margin: 0 auto;

    background: #ffffff;

    border-radius: 20px;

    padding: 32px 28px;

    box-shadow: 0 10px 28px rgba(0,0,0,0.08);

    display: flex;
    flex-direction: column;

    gap: 18px;
}

/* 타이틀 */
.login-title {
    font-size: 24px;
    font-weight: 700;
    text-align: center;
    color: #1e293b;
    margin-bottom: 8px;
}

/* input */
.form-group input {

    width: 100%;
    height: 46px;

    padding: 0 14px;

    border-radius: 12px;

    border: 1px solid #e0e0e0;

    font-size: 14px;

    outline: none;

    transition: all 0.2s;
}

.form-group input:focus {
    border-color: #90caf9;
    box-shadow: 0 0 0 2px rgba(144,202,249,0.2);
}

/* 로그인 버튼 */
.login-btn {

    width: 100%;
    height: 46px;

    border: none;

    border-radius: 14px;

    background: linear-gradient(
        135deg,
        #e3f2fd,
        #bbdefb
    );

    color: #0d47a1;

    font-size: 15px;
    font-weight: 600;

    cursor: pointer;

    transition: all 0.2s;
}

.login-btn:hover {
    background: linear-gradient(
        135deg,
        #bbdefb,
        #90caf9
    );
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.login-btn:active {
    transform: scale(0.98);
}

/* 회원가입 링크 */
.signup-link {

    text-align: center;

    font-size: 13px;

    color: #616161;
}

.signup-link a {

    margin-left: 4px;

    color: #1976d2;

    text-decoration: none;

    font-weight: 600;
}

.signup-link a:hover {
    text-decoration: underline;
}
</style>