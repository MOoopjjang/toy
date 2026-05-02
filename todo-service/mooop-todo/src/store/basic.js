import { defineStore } from 'pinia'


export const basicStore = defineStore('basic-store-id',{
    state: ()=>({
        status:{
            data:[],
            empty:true
        },
        category:{
            data:[],
            empty:true
        }
    }),
    actions:{
        setStatus(_data){
             if(_data === null || _data === undefined || _data.length === 0){
                return
            }
            if(!this.status.empty){
                this.status.data = []
            }
            _data.forEach((d)=>{
                this.status.data.push(d)
            })
            this.status.empty = false
        },
        setCategory(_data){
             if(_data === null || _data === undefined || _data.length === 0){
                return
            }
            if(!this.category.empty){
                this.category.data = []
            }
            _data.forEach((d)=>{
                this.category.data.push(d)
            })
            this.category.empty = false
        }
    }
})