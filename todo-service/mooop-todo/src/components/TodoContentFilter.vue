<script setup>
import { onMounted, ref , getCurrentInstance, reactive } from 'vue';
// import { userAuthStore } from '../store/auth'
import {basicStore} from '../store/basic'
import {memberStore} from '../store/member'
import {todoStore} from '../store/Todo'

import {ApiUrlCode , ApiResultCode } from '../code/ApiCode'

const { proxy } = getCurrentInstance()
// const authInfoStore = userAuthStore()
const basicInfoStore = basicStore()
const memberInfoStore = memberStore()
const todoInfoStore = todoStore()


const todoFindRequest = {
    "memberId":memberInfoStore.memberId,
    "todoId":null,
    "todoTitle":null,
    "todoCategory":null,
    "todoStatus":null,
    "startDay":null,
    "endDay":null,
    "page":1,
    "size":-1
}



const handleApiError = (e)=>{
    if(!e.response){
        alert("네트워크 오류가 발생했습니다.")
        return
    }

    switch(e.response.status){
        case 400:
            alert("내부 에러가 발생했습니다.")
            break
        case 404:
            alert("리소스를 찾을수 없습니다.")
            break
        default:
            console.error('Unhandled error')
    }
}


const statusInfoApiCall = ()=>{
    proxy.$api.get(ApiUrlCode.STATUS_INFO)
    .then((response)=>{
        console.dir(response)
        if(response.data.result === ApiResultCode.SUCCESS){
            basicInfoStore.setStatus(response.data.body)
        }else{
            alert(response.data.message)
        }
    }).catch(handleApiError)
}

const categoryInfoApiCall = ()=>{
    proxy.$api.get(ApiUrlCode.CATEGORY_INFO)
    .then((response)=>{
        console.dir(response)
        if(response.data.result === ApiResultCode.SUCCESS){
            basicInfoStore.setCategory(response.data.body)
        }else{
            alert(response.data.message)
        }
    }).catch(handleApiError)
}




const onSearch = ()=>{
    const token = authInfoStore.getToken()
    todoFindRequest.memberId = memberInfoStore.memberId
     todoFindRequest.startDay = document.querySelector("#start-date").value;
     todoFindRequest.endDay = document.querySelector("#end-date").value;
     todoFindRequest.todoStatus = document.querySelector("#status-select > option:checked").value
     todoFindRequest.todoCategory = document.querySelector("#category-select > option:checked").value
     console.dir(todoFindRequest)

    proxy.$api.post(ApiUrlCode.SEARCH_TODO, todoFindRequest)
    .then((response)=>{
        if(response.data.result === ApiResultCode.SUCCESS){
            console.dir(response.data.body)
            todoInfoStore.setTodoList(response.data.body)
        }else{
            alert(response.data.message)
        }
    }).catch(handleApiError)
}


onMounted(()=>{
    if(basicInfoStore.status.empty){
        statusInfoApiCall()
    }

    if(basicInfoStore.category.empty){
        categoryInfoApiCall()
    }
    
    
})


</script>
<template>
    <!-- ===================== -->
        <!-- Filter 영역 -->
        <!-- ===================== -->
        <div class="filter-card">

<!--
    <div class="filter-title">
        Filter
    </div>
    -->

    <!-- 시작일 / 종료일 -->
    <div class="filter-row">

        <div class="filter-group">
            <label>시작일</label>
            <input id="start-date" type="date">
        </div>

        <div class="filter-group">
            <label>종료일</label>
            <input id="end-date" type="date">
        </div>

    </div>

    <!-- 진행상태 / category -->
    <div class="filter-row">

        <div class="filter-group">
            <label>진행상태</label>
            <select id="status-select">
                <option v-for="(v,i) in basicInfoStore.status.data" :key="i" :value="v">{{ v }}</option>
            </select>
        </div>

        <div class="filter-group">
            <label>Category</label>
            <select id="category-select">
                <option v-for="(v,i) in basicInfoStore.category.data" :key="i" :value="v">{{ v }}</option>
            </select>
        </div>

    </div>

    <div class="filter-actions">
        <button class="btn-reset">초기화</button>
        <button class="btn-apply" v-on:click="onSearch">적용</button>
    </div>

</div>

</template>
<style scoped>
/* Filter Card */
.filter-card {

    background: #ffffff;

    border-radius: 16px;

    padding: 16px;

    margin-bottom: 20px;

    box-shadow: 0 2px 8px rgba(0,0,0,0.08);

    display: flex;
    flex-direction: column;

    gap: 14px;
}

/* 제목 */
.filter-title {

    font-size: 16px;

    font-weight: 600;

    color: #212121;
}

/* row layout */
.filter-row {

    display: grid;

    grid-template-columns: 1fr 1fr;

    gap: 12px;
}

/* group */
.filter-group {

    display: flex;

    flex-direction: column;

    text-align: left;  /* label 좌측 정렬 */
}

.filter-group label {

    font-size: 13px;

    color: #616161;

    margin-bottom: 4px;

    text-align: left;
}

/* input */
.filter-group input,
.filter-group select {

    height: 36px;

    padding: 6px 8px;

    border-radius: 8px;

    border: 1px solid #e0e0e0;

    font-size: 13px;

    outline: none;
}

/* focus */
.filter-group input:focus,
.filter-group select:focus {

    border-color: #64b5f6;
}

/* 버튼 영역 */
.filter-actions {

    margin-top: 4px;

    display: flex;

    justify-content: flex-end;

    gap: 10px;
}
</style>
