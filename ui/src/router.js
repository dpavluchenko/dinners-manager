import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/sign-in',
      name: 'sign-in',
      component: () => import('./components/SignIn')
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: () => import('./components/SignUp')
    }
  ]
})
