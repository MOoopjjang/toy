import { defineStore } from 'pinia';
export const headerStore = defineStore('header-store-id',{
    state: ()=>({
        userId:'',
        memberId:0,
        profileImage:'',
        completeCount:0,
        ongoingCount:0,
        expireCount: 0,
        holdCount: 0,
        readyCount: 0
    }),
    actions:{
        setStatusInfo(_headerInfo){
            console.dir(_headerInfo);
            Object.assign(this , _headerInfo);
        }
    }
})