import Vue from 'vue'
import Router from 'vue-router'
import {isAuthenticated, hasManagerRole} from './helpers/checkUser'
import Personal from './views/Personal'
import Order from './components/personal/OrderStatistic'
import Menu from './components/personal/Menu'
import UserList from './components/personal/UserList'
import GroupList from './components/personal/GroupList'


Vue.use(Router)

function requireAuth(to, from, next) {
    isAuthenticated().then(() => next()).catch(() => next('/sign-in'))
}

function requireRole(to, from, next) {
    hasManagerRole().then(() => next()).catch(() => next('/menu'))
}

function chooseRoute(to, from, next) {
    isAuthenticated().then(() => next('/menu')).catch(() => next('/sign-in'))
}

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'root',
            beforeEnter: chooseRoute
        },
        {
            path: '/sign-in',
            name: 'sign-in',
            component: () => import('./components/SignIn')
        },
        {
            path: '/sign-up',
            name: 'sign-up',
            component: () => import('./components/SignUp')
        },
        {
            path: '/menu',
            name: 'order-menu',
            component: () => import('./views/OrderMenu'),
            beforeEnter: requireAuth
        },
        {
            path: '/personal',
            name: 'personal',
            component: Personal,
            beforeEnter: requireRole,
            redirect: '/personal/orders',
            children: [
                {
                    path: 'orders',
                    name: 'orders',
                    components: {
                        personal: Order
                    }
                },
                {
                    path: 'manage-menu',
                    name: 'manage-menu',
                    components: {
                        personal: Menu
                    }
                },
                {
                    path: 'users',
                    name: 'users',
                    components: {
                        personal: UserList
                    }
                },
                {
                    path: 'groups',
                    name: 'groups',
                    components: {
                        personal: GroupList
                    }
                }
            ]
        },
        {
            path: '*',
            name: 'page-not-found',
            component: () => import('./views/PageNotFound')
        }
    ]
})
