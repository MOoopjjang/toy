<script setup>
import { onMounted , onUnmounted , ref , getCurrentInstance, reactive } from 'vue';
import { useRouter } from 'vue-router'
import { memberStore } from '../store/member'
import {todoStore} from '../store/todo'
import {basicStore} from '../store/basic'

import {ApiUrlCode , ApiResultCode} from '../code/ApiCode'
import {AppCode} from '../code/AppCodes'


const todoInfoStore = todoStore()
const memberInfoStore = memberStore()
const basicInfoStore = basicStore()
const { proxy } = getCurrentInstance()
const router = useRouter()


const contentBody = reactive({
    "memberId":memberInfoStore.memberId,
    "todoId":0,
    "title":"",
    "content":"",
    "category":"",
    "status":"",
    "startAt":"",
    "completeAt":"",
    "priority":1

})
const actionMode = ref(AppCode.DETAIL_ACTION_MODE_REG)

const statusInfo = ref([])



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


const categoryInfoApiCall = async ()=>{
    return proxy.$api.get(ApiUrlCode.CATEGORY_INFO)
}




const setTodoForm = ()=>{
    actionMode.value = AppCode.DETAIL_ACTION_MODE_VIEW

    const categoryE = document.querySelector('#category-select')
    const statusE = document.querySelector('#status-select')
    // console.dir(categoryE)

    contentBody.memberId = todoInfoStore.selectTodo.memberId
    contentBody.todoId = todoInfoStore.selectTodo.todoId
    contentBody.title = todoInfoStore.selectTodo.title
    contentBody.content = todoInfoStore.selectTodo.content
    contentBody.priority = todoInfoStore.selectTodo.detail.priority
    contentBody.status = todoInfoStore.selectTodo.detail.status

    document.querySelector('#start-date').value = todoInfoStore.selectTodo.detail.registerStartAt
    document.querySelector('#end-date').value = todoInfoStore.selectTodo.detail.registerCompleteAt


    statusInfo.value = []
    basicInfoStore.status.data.forEach(s=>{
         statusInfo.value.push(s)
    })

    /** TODO : 화면이 모두 그려진후에 select set ->  timeout가 아닌 다른 방법 필요!!! */
    setTimeout(()=>{
        console.log(`status = `, todoInfoStore.selectTodo.detail.status)
        categoryE.value = todoInfoStore.selectTodo.category
        statusE.value = todoInfoStore.selectTodo.detail.status
    },100)
}


const onCancelRegister = ()=>{
    // todoInfoStore.unselected()
    router.push({name:"main"})
}

const onRegister = async ()=>{
    try{
        contentBody.startAt = document.querySelector("#start-date").value
        contentBody.completeAt = document.querySelector("#end-date").value
        contentBody.category = document.querySelector("#category-select > option:checked").value

        const registerRes = await proxy.$api.post(ApiUrlCode.TODO_REG , contentBody)
        console.dir(registerRes)

        if(registerRes.data.body.result === "FAIL"){
            alert(registerRes.data.body.message)
            return
        }

        alert("등록되었습니다.")

        // todoInfoStore.unselected()

        /** header정보 update 요청 */
        proxy.$emitter.emit('headerUpdate' , 'update')

        router.push({name:"main"})
    }catch(error){
        console.dir(error)
        
    }
}


const onUpdate = async ()=>{
    const updateRequest = {
        "memberId":contentBody.memberId,
        "todoId":contentBody.todoId,
        "title":contentBody.title,
        "content":contentBody.content,
        "category":document.querySelector("#category-select > option:checked").value,
        "status":document.querySelector("#status-select > option:checked").value,
        "startAt":document.querySelector("#start-date").value,
        "completeAt":document.querySelector("#end-date").value,
        "priority":contentBody.priority
    }


    proxy.$api.post(ApiUrlCode.TODO_UPDATE , updateRequest)
    .then((response)=>{
        console.dir(response)
        if(response.data.result === ApiResultCode.SUCCESS){
            alert('변경하였습니다.')

            /** header정보 update 요청 */
            proxy.$emitter.emit('headerUpdate' , 'update')
            router.push({name:"main"})
        }else{
            alert(response.data.message)
        }

    }).catch(handleApiError)
}



onMounted(async ()=>{
    console.log('onMounted call')
    if(basicInfoStore.status.empty){
        const categoryRes = await categoryInfoApiCall()
        basicInfoStore.setCategory(categoryRes.data.body)
    }

    if(todoInfoStore.isSelect){
       setTodoForm()
    }
})


onUnmounted(()=>{
    todoInfoStore.unselected()
})
</script>
<template>
    <section class="todo-content">

    <div class="todo-form-card">

        <!-- 완료 상태 안내 -->
        <div v-show="contentBody.status === 'COMPLETE'" class="todo-complete-banner" >
            완료된 작업입니다.
        </div>

        <!-- 제목 -->
        <div class="form-group"  >
            <label>제목</label>
            <input type="text" placeholder="Todo 제목을 입력하세요" v-model="contentBody.title" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''" />
        </div>


        <!-- 날짜 -->
        <div class="form-row">
            <div class="form-group">
                <label>시작날짜</label>
                <input id="start-date" type="date" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''" >
            </div>

            <div class="form-group">
                <label>완료날짜</label>
                <input id="end-date" type="date" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''" >
            </div>
        </div>


        <!-- 카테고리 / 우선순위 -->
        <div class="form-row">
            <div class="form-group">
                <label>카테고리</label>
                <select id="category-select" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''" >
                    <option v-for="(v,i) in basicInfoStore.category.data" :key="i" :value="v">{{ v }}</option>
                </select>
            </div>
            <div class="form-group">
                <label>우선순위</label>
                <input type="number" min="1" max="5" placeholder="1~5" v-model="contentBody.priority"  v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''" >
            </div>
        </div>


        <!-- 진행상태 -->
        <div class="form-row" v-show="actionMode === AppCode.DETAIL_ACTION_MODE_VIEW">
            <div class="form-group">
                <label>진행상태</label>
                <select id="status-select" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''" >
                    <option v-for="(v,i) in statusInfo" :key="i" :value="v">{{ v }}</option>
                </select>
            </div>
        </div>


        <!-- 본문 -->
        <div class="form-group">
            <label>본문</label>
            <textarea placeholder="Todo 상세 내용을 입력하세요" v-model="contentBody.content" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''" ></textarea>
        </div>


        <!-- 버튼 -->
        <div class="form-actions">
            <button class="btn-cancel" v-on:click="onCancelRegister">뒤로가기</button>

            <button v-if="actionMode === AppCode.DETAIL_ACTION_MODE_REG" class="btn-save" v-on:click="onRegister" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''">저장</button>
            <button v-else-if="actionMode === AppCode.DETAIL_ACTION_MODE_VIEW" class="btn-save" v-on:click="onUpdate" v-bind:class="contentBody.status === 'COMPLETE'?'m-disabled':''">수정</button>
        </div>

    </div>

</section>
</template>
<style scoped>
/* 완료 상태 배너 */
.todo-complete-banner {

    width: 90%;

    padding: 10px 14px;

    border-radius: 10px;

    font-size: 13px;
    font-weight: 600;

    color: #2e7d32;  /* material green */

    background: linear-gradient(
        135deg,
        #e8f5e9,
        #c8e6c9
    );

    border: 1px solid #a5d6a7;

    display: flex;
    align-items: center;

    gap: 6px;
}

/* 아이콘 느낌 (선택사항) */
.todo-complete-banner::before {
    content: "✔";
    font-size: 14px;
}

.todo-content {
    padding: 16px;
    background: #f5f6f8;
}

.todo-form-card {

    background: #ffffff;
    border-radius: 16px;

    padding: 20px;

    box-shadow: 0 3px 10px rgba(0,0,0,0.08);

    display: flex;
    flex-direction: column;

    gap: 18px;
}

/* form group */
.form-group {

    display: flex;
    flex-direction: column;

    text-align: left;   /* label 좌측 정렬 */
}

.form-group label {

    font-size: 13px;
    color: #616161;

    margin-bottom: 6px;

    text-align: left;   /* 명시적으로 좌측정렬 */
}

/* input 공통 */
.form-group input,
.form-group select,
.form-group textarea {

    border: 1px solid #e0e0e0;

    border-radius: 8px;

    padding: 10px;

    font-size: 14px;

    outline: none;

    transition: border 0.2s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {

    border-color: #64b5f6;
}

/* 제목 강조 */
.form-group input[type="text"] {

    font-size: 16px;
    font-weight: 600;
}

/* 2열 grid */
.form-row {

    display: grid;

    grid-template-columns: 1fr 1fr;

    gap: 12px;
}

/* textarea */
textarea {

    min-height: 120px;

    resize: vertical;
}

/* 버튼 영역 */
.form-actions {

    display: flex;

    justify-content: flex-end;

    gap: 10px;

    margin-top: 6px;
}

.btn-cancel {

    padding: 8px 16px;

    border-radius: 8px;

    border: none;

    background: #eeeeee;

    color: #616161;

    cursor: pointer;
}

.btn-save {

    padding: 8px 18px;

    border-radius: 8px;

    border: none;

    background: linear-gradient(
        135deg,
        #e3f2fd,
        #bbdefb
    );

    color: #0d47a1;

    font-weight: 600;

    cursor: pointer;
}
</style>
