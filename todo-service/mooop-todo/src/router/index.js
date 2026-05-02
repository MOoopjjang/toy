import { createRouter , createWebHistory } from 'vue-router'
import TodoHeader from '../components/TodoHeader.vue'
import TodoContent from '../components/TodoContent.vue'
import TodoFooter from '../components/TodoFooter.vue'
import TodoForm from '../components/TodoForm.vue'
import MemberForm from '../components/MemberForm.vue'
import TodoLogin from '../views/TodoLogin.vue'


const routes = [
    {
        path: '/',
        name: 'main',
        components: {
            default:TodoContent,
            header: TodoHeader,
            footer: TodoFooter
        }
    },
    {
        path: '/register',
        name: 'register',
        components: {
            default:TodoForm,
            header: TodoHeader,
            footer: TodoFooter
        }

    },
    {
        path: '/login',
        name: 'login',
        component: TodoLogin
    },
    {
        path: '/member/register',
        name: 'memberRegister',
        component: MemberForm
    }
    // {
    //     path: '/member/modify',
    //     name: 'memberModify',
    //     components: {
    //         default:MemberForm,
    //         header: TodoHeader,
    //         footer: TodoFooter
    //     }
    // }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})


export default router