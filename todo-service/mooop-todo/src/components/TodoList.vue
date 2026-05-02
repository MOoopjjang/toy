<script setup>
import { onMounted , getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
import {todoStore} from '../store/todo'

import { ApiResultCode , ApiUrlCode } from '../code/ApiCode'
import { AppCode } from '../code/AppCodes'

const { proxy } = getCurrentInstance()
const todoInfoStore = todoStore()
const router = useRouter()



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


const contentListInfoApiCall = ()=>{
    proxy.$api.get(ApiUrlCode.CONTENT_INFO)
    .then((response)=>{
        todoInfoStore.setTodoList(response.data.body)
    }).catch(handleApiError)
}

const onTodoDetail = (_index)=>{
    todoInfoStore.selected()
    // TODO : TodoForm에서 API 통신을 통해 정보를 취득하도록 변경 필요.!!!!
    todoInfoStore.setSelectTodo( todoInfoStore.todoList[_index] )

    router.push({name:"register"})
}


const onCheckChange = (v)=>{
    console.dir(v)
}

const onDelete = async(_data)=>{

    const checkedTodoList = todoInfoStore.todoList.filter(v=>v.checked)
    if(checkedTodoList.length === 0){
        alert('삭제할 항목을 선택해 주세요.')
        return
    }

     const deleteTodoList = checkedTodoList.map(v=>v.todoId)
        const todoDeleteRequest = {
            "todoList":deleteTodoList
        }
        proxy.$api.post(ApiUrlCode.CONTENT_DELETE , todoDeleteRequest)
        .then((apiResponse)=>{
            if(apiResponse.data.result === ApiResultCode.SUCCESS){
                alert("삭제되었습니다.")
                contentListInfoApiCall(localStorage.getItem(AppCode.AUTH_TOKEN))
            }else{
                alert(apiResponse.data.message);
            }
        }).catch(handleApiError)

    
    
}


onMounted( ()=>{
    contentListInfoApiCall()


    proxy.$emitter.on('delete' , onDelete)

    // TODO : 아래코드가 동작하지 않는 이유...???
    /*
     const contentInfoRes = await contentListInfoApiCall(authInfoStore.getToken())
     console.dir(contentInfoRes)
     todoInfoStore.setTodoList(contentInfoRes.data.body)
     */
})

</script>
<template>
    <!-- ===================== -->
    <!-- 기존 Todo List -->
    <!-- ===================== -->

<div class="todo-list-container">
    <div class="todo-item" v-for="(v,i) in todoInfoStore.todoList" :key="i" :value="v" v-on:click="onTodoDetail(i)">
        <div class="todo-grid">

            <input type="hidden" name="todoId" v-model="v.todoId" />
            <div class="todo-check">
                <input type="checkbox" v-model="v.checked" @click.stop @change="onCheckChange(v)" />
            </div>

            <div class="todo-main">

                <div class="todo-row-top">
                    <div class="todo-date">
                        {{ v.detail.registerStartAt }} ~ {{ v.detail.registerCompleteAt }}
                    </div>
                    <div class="todo-progress">
                        진행율 : {{ v.detail.progress }}%
                    </div>
                </div>

                <div class="todo-title">
                    {{ v.title }}
                </div>

                <div class="todo-category">
                    {{ v.category }}
                </div>

            </div>

            <div class="todo-side">
                <div class="todo-status status-progress">
                    {{ v.detail.status }}
                </div>
            </div>

        </div>
    </div>
</div>
    
</template>
<style scoped>
/* ========================= */
/* 기존 Todo Card 유지 */
/* ========================= */

.todo-item {
    background: #ffffff;
    border-radius: 16px;
    padding: 16px;
    margin-bottom: 16px;

    box-shadow: 0 2px 8px rgba(0,0,0,0.08);

    cursor: pointer; /* 클릭 가능 표시 */

    transition: all 0.2s ease;
}

/* hover 효과 (PC) */
.todo-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0,0,0,0.12);
}

/* 클릭 효과 */
.todo-item:active {
    transform: scale(0.98);
    background: #f5f9ff;
}

.todo-grid {
    display: grid;
    grid-template-columns: 32px 1fr auto;
    column-gap: 12px;
    align-items: center;
}

.todo-main {
    display: flex;
    flex-direction: column;
    gap: 6px;
    text-align: left;
}

.todo-row-top {
    display: flex;
    justify-content: space-between;
}

.todo-date {
    font-size: 12px;
    color: #757575;
}

.todo-progress {
    font-size: 12px;
    color: #616161;
}

.todo-title {
    font-size: 18px;
    font-weight: 700;
    color: #212121;
}

.todo-category {
    font-size: 13px;
    color: #9e9e9e;
}

.todo-status {
    font-size: 12px;
    font-weight: 600;
    padding: 4px 6px;
    border-radius: 10px;
    text-align: center;
    min-width: 60px;
}

.status-complete {
    background: #fff3e0;
    color: #ef6c00;
}

.status-progress {
    background: #e3f2fd;
    color: #1976d2;
}

.status-expired {
    background: #ffebee;
    color: #d32f2f;
}
</style>
