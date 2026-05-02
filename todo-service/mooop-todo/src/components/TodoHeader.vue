<template>
    <div>

        <!-- 사이드 메뉴 -->
        <!-- <nav class="side-menu" id="sideMenu">
            <div class="side-header">
                <span>메뉴</span>
                <button class="close-btn" v-on:click="toggleSideMenu()">
                    <span class="material-symbols-outlined">close</span>
                </button>
            </div>
            <div class="side-item">Dashboard</div>
            <div class="side-item">My Todo</div>
            <div class="side-item">Settings</div>
        </nav> -->
        <div class="overlay" id="overlay" v-on:click="toggleSideMenu()"></div>
        <header class="todo-header">
            <!-- 1 Line -->
            <div class="header-line header-top">
                <button class="hamburger-btn" v-on:click="toggleSideMenu()">
                    <span class="material-symbols-outlined">menu</span>
                </button>

                <div class="right-area">
                    <div class="profile-wrapper">
                        <button class="profile-btn" v-on:click="toggleProfileMenu()">
                            <img :src="headerInfoStore.profileImage" class="profile-img" />
                        </button>

                        <div class="dropdown-menu" id="profileMenu" v-show="isMenuOpen">
                            <div class="menu-item" v-on:click="openMember">Profile</div>
                            <div class="menu-item" v-on:click="onLogout">Logout</div>
                        </div>
                    </div>
                    <span class="nickname">{{ headerInfoStore.userId }}</span>
                </div>
            </div>

            <!-- 2 Line -->
            <div class="header-line header-date">
                2026년 2월 24일 (화)
            </div>

            <!-- 3 Line -->
            <div class="header-line header-status">
                <div>완료 : <b>{{ headerInfoStore.completeCount }}</b></div>
                <div>진행중 : <b>{{ headerInfoStore.ongoingCount }}</b></div>
                <div>만료 : <b>{{ headerInfoStore.expireCount }}</b></div>
                <div>중단 : <b>{{ headerInfoStore.holdCount }}</b></div>
                <div>대기 : <b>{{ headerInfoStore.readyCount }}</b></div>
            </div>
        </header>
    </div>

    <MemberForm :mode="modeType" v-if="isMemberFormOpen" @close="isMemberFormOpen = false" />
    
</template>

<script setup>
import { onMounted , onUnmounted , getCurrentInstance , ref } from 'vue'
import { useRouter } from 'vue-router'
import {headerStore} from '../store/header'
import {memberStore} from '../store/member'
import { AppCode } from '../code/AppCodes'

import MemberForm from './MemberForm.vue'


const { proxy } = getCurrentInstance()
const router = useRouter()
const headerInfoStore = headerStore()
const memberInfoStore = memberStore()

const isMenuOpen = ref(false)
const isMemberFormOpen = ref(false)
const modeType=ref('MODIFY')

const toggleProfileMenu = ()=>{
    console.log(isMenuOpen.value)
    isMenuOpen.value = true
    document.getElementById("profileMenu").classList.toggle("show")
}

const toggleSideMenu = ()=>{
    closeMenu()
    document.getElementById("sideMenu").classList.toggle("open")
    document.getElementById("overlay").classList.toggle("show")
}

const closeMenu = ()=>{
    isMenuOpen.value = false
}

const openMember = ()=>{
    toggleProfileMenu()
    isMemberFormOpen.value = true
}

const onLogout = ()=>{
    closeMenu()
    localStorage.removeItem(AppCode.AUTH_TOKEN)
    router.push({name:'login'})
}


const handleApiError = (e)=>{
    if(!e.response){
        alert('네트워크 오류')
        return
    }

    const status = e.response.status
    switch(status){
        case 400:
            alert('내부 오류 발생')
            break
        case 404:
            alert('404')
            break
        default:
            console.error('unhandle error')
    }
}

const headerInfoApiCall = async ()=>{
    return proxy.$api.get('/header')
}

const memberInfoApiCall =  ()=>{
    return proxy.$api.get('/member/info')
    .then((response)=>{
        console.dir(response)
        memberInfoStore.setMemberInfo(response.data.body)
    }).catch(handleApiError)
}

const headerInfoUpdatehandler = async (value)=>{
    const headerRes = await headerInfoApiCall()
    console.dir(headerRes)
    headerInfoStore.setStatusInfo(headerRes.data.body)
}




onMounted(()=>{
    console.log('TodoHeader > onMounted')
    headerInfoUpdatehandler("")
    memberInfoApiCall()
    /** Event 수신  */
    proxy.$emitter.on('headerUpdate' , headerInfoUpdatehandler)
})

onUnmounted(()=>{
    proxy.$emitter.off('headerUpdate' , headerInfoUpdatehandler)
})

</script>

<style scoped>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

body {
    background: #f5f6f8;
}

/* Header */
.todo-header {
    background: white;
    padding: 12px 16px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.header-line {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.header-top {
    height: 48px;
}

/* 햄버거 버튼 */
.hamburger-btn {
    background: none;
    border: none;
    font-size: 28px;
    cursor: pointer;
}

/* 오른쪽 영역 */
.right-area {
    display: flex;
    align-items: center;
    gap: 8px;
}

.nickname {
    font-weight: 600;
    font-size: 15px;
}

/* 날짜 */
.header-date {
    margin-top: 4px;
    height: 28px;
    font-size: 13px;
    color: #666;
}

/* 상태 */
.header-status {
    margin-top: 8px;
    gap: 10px;
    font-size: 13px;
    flex-wrap: wrap;
}

.header-status div {
    background: #f2f3f7;
    padding: 4px 8px;
    border-radius: 8px;
}

.header-status b {
    color: #2c7be5;
}

/* 프로필 */
.profile-wrapper {
    position: relative;
}

.profile-btn {
    border: none;
    background: none;
    cursor: pointer;
}

.profile-img {
    width: 32px;
    height: 32px;
    border-radius: 50%;
}

.dropdown-menu {
    position: absolute;
    right: 0;
    top: 42px;
    background: white;
    width: 120px;
    border-radius: 10px;
    box-shadow: 0 4px 14px rgba(0,0,0,0.1);
    display: none;
    flex-direction: column;
}

.dropdown-menu.show {
    display: flex;
}

.menu-item {
    padding: 10px 14px;
    cursor: pointer;
    font-size: 14px;
}

.menu-item:hover {
    background: #f0f2f5;
}

/* 사이드 메뉴 */
.side-menu {
    position: fixed;
    top: 0;
    left: -260px;
    width: 240px;
    height: 100%;
    background: white;
    box-shadow: 2px 0 12px rgba(0,0,0,0.1);
    transition: left 0.3s ease;
    z-index: 1001;
    padding: 16px;
}

.side-menu.open {
    left: 0;
}

.side-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.close-btn {
    background: none;
    border: none;
    font-size: 24px;
    cursor: pointer;
}

.side-item {
    padding: 12px 8px;
    border-radius: 8px;
    cursor: pointer;
}

.side-item:hover {
    background: #f2f3f7;
}

/* 오버레이 */
.overlay {
    position: fixed;
    inset: 0;
    background: rgba(0,0,0,0.3);
    display: none;
    z-index: 1000;
}

.overlay.show {
    display: block;
}
</style>
