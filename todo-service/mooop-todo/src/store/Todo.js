import { defineStore } from 'pinia'

export const todoStore = defineStore('todo-store-id',{
    state: ()=>({
        todoList: [],
        selectTodo:{},
        isSelect:false
    }),
    actions:{
        setTodoList(_todoList){
            console.log(`_todoList size = `,_todoList.length)
            if(this.todoList.length > 0){
                this.todoList = []
            }
            _todoList.forEach(todo=>{
                this.todoList.push(todo)
            })
        },
        setSelectTodo(_todoInfo){
            this.selectTodo = _todoInfo
        },
        selected(){
            this.isSelect = true
        },
        unselected(){
            this.isSelect = false
        }
    }
})