import { defineStore } from 'pinia'


export const userAuthStore = defineStore('auth-store-id',{
    state: ()=>({
        token:'',
        userId:'cwkim',
        password: '1111'
    }),
    actions:{
        getToken(){return this.token;},
        setToken(_token){
            this.token = _token;
        },
        isAuthentication(){
            return this.token !== '';
        },
        clearAuthentication(){
            this.token = ''
        }
    }
})