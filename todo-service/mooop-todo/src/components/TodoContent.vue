<style scoped>
.todo-content {
    padding: 16px;
    background: #f5f6f8;
}



.btn-reset,
.btn-apply {
    padding: 8px 14px;
    border-radius: 8px;
    font-size: 13px;
    cursor: pointer;
    border: none;
}

.btn-reset {
    background: #eeeeee;
    color: #616161;
}

.btn-apply {
    background: #1976d2;
    color: #ffffff;
}

/**------------- */
.todo-action-row {

    display: flex;

    gap: 10px;

    margin-bottom: 16px;
}


/* 공통 버튼 스타일 */
.todo-action-row button {

    flex: 1;   /* 🔥 동일한 width */

    height: 40px;

    border: none;

    border-radius: 12px;

    font-size: 20px;

    font-weight: 600;

    display: flex;
    align-items: center;
    justify-content: center;

    cursor: pointer;

    transition: all 0.2s ease;
}


/* + 버튼 */
.todo-add-btn {

    background: linear-gradient(
        135deg,
        #e3f2fd,
        #bbdefb,
        #90caf9
    );

    color: #1e3a8a;

    box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.todo-add-btn:hover {

    background: linear-gradient(
        135deg,
        #dbeafe,
        #93c5fd,
        #60a5fa
    );

    box-shadow: 0 4px 10px rgba(0,0,0,0.12);
}


/* 🔥 삭제 버튼 (붉은 계열 그라데이션) */
.todo-del-btn {

    background: linear-gradient(
        135deg,
        #fdecea,
        #f8b4b4,
        #f87171
    );

    color: #7f1d1d;

    box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.todo-del-btn:hover {

    background: linear-gradient(
        135deg,
        #fcd5d5,
        #f87171,
        #ef4444
    );

    box-shadow: 0 4px 10px rgba(0,0,0,0.12);
}


/* 클릭 효과 */
.todo-action-row button:active {
    transform: scale(0.97);
}

/**--------------------- */
</style>
<template>
    <section class="todo-content">
        <TodoFilter />
        <div class="todo-action-row">
            <button class="todo-add-btn" v-on:click="goToRegister">+</button>
            <button class="todo-del-btn" v-on:click="onDelete">-</button>
        </div>
        <TodoList />
    </section>
</template>
<script setup>
import { onMounted , onUnmounted , getCurrentInstance , ref } from 'vue'
import { useRouter } from 'vue-router'
import TodoFilter from './TodoContentFilter.vue'
import TodoList from './TodoList.vue'

const {proxy} = getCurrentInstance()
const router = useRouter()

const goToRegister = ()=>{
    router.push({name:'register'})
}

const onDelete = ()=>{
    if(confirm("체크된 항목을 삭제하시겠습니까?")){
        proxy.$emitter.emit('delete' , '')
    }
   
}
</script>
