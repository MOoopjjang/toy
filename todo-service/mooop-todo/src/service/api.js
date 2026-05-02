import axios from 'axios'
import router from '../router/index.js'
import { AppCode } from '../code/AppCodes'

const api = axios.create({
    baseURL: 'http://localhost:8080'
})


api.interceptors.request.use(
    config => {
        const token = localStorage.getItem(AppCode.AUTH_TOKEN)
        // config.headers['Content-Type'] = 'application/json'
        config.headers['Accept'] = 'application/json'
        console.log(`[request] url : ${config.url} , token = ${token}`)
        if(token){
            config.headers['x-auth-token'] = token
        }
        return config
    },
    error => {
        console.dir(error)
        return Promise.reject(error)
    }
)


api.interceptors.response.use(
    response => {
        console.log(`[response] url : ${response.url}`)
        return response
    },
    error => {
        console.dir(error)
        console.log(`[error] url : ${error.url} , error : ${error.response.status}`)
        if(error.response){
            const status = error.response.status
            if(status === 401){
                localStorage.removeItem(AppCode.AUTH_TOKEN)
                router.push({path:'/login'})
            }else if(status === 403 ){
                alert('접근권한이 없습니다.관리자에게 문의하세요.')
            }else if(status >= 500){
                alert('서버오류가 발생했습니다.잠시후에 다시 시도해 주세요.')
            }
        }

        return Promise.reject(error)        
    }
)


export default api

