import { defineStore } from 'pinia'

export const memberStore = defineStore('member-store-id',{
    state: ()=>({
        memberId:0,
            email:'',
            userid: '',
            status:'',
            address:'',
            profileImage:'',
            registeredAt: '',
            activatedAt: '',
            withdrawAt: ''
    }),
    actions:{
        setMemberInfo(_data){
            console.dir(_data)
            console.dir(this)
            Object.assign(this , _data)
        }
    }
}) 