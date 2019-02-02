import qs from 'qs'
import Vue from 'vue'

export default {
    state: {
        user: {
            isAuthenticated: null,
            role: null,
            name: null
        }
    },
    mutations: {
        setUserParams(state, payload) {
            state.user.isAuthenticated = true
            state.user.role = payload.role
            state.user.name = payload.name
        },
        resetUserParams(state) {
            state.user.isAuthenticated = null
            state.user.role = null
            state.user.name = null
        }
    },
    actions: {
        signIn(context, userData) {
            return new Promise((resolve, reject) => {
                const options = {
                    method: 'POST',
                    headers: {'content-type': 'application/x-www-form-urlencoded'},
                    data: qs.stringify(userData),
                    url: '/api/login'
                }
                Vue.axios(options).then((response) => {
                    context.commit('setUserParams', response.data)
                    resolve(response)
                })
                    .catch((error) => {
                        reject(error)
                    })
            })
        },
        signUp(context, userData) {
            return new Promise((resolve, reject) => {
                const options = {
                    method: 'POST',
                    data: userData,
                    url: '/api/register'
                }
                Vue.axios(options).then((response) => {
                    context.commit('setUserParams', response.data)
                    resolve(response)
                }).catch((error) => {
                    reject(error)
                })
            })
        },
        signOut(context) {
            return new Promise((resolve, reject) => {
                Vue.axios({url: '/api/logout'}).then((response) => {
                    resolve(response)
                    context.commit('resetUserParams')
                    context.commit('clearNewOrders')
                }).catch((error) => reject(error))
            })
        },
        checkAuth(context) {
            return new Promise((resolve, reject) => {
                Vue.axios({url: '/api/login'}).then((response) => {
                    context.commit('setUserParams', response.data)
                    resolve(response)
                }).catch((error) => {
                    context.commit('resetUserParams')
                    reject(error)
                })
            })
        }
    },
    getters: {
        isAuthenticated: state => state.user.isAuthenticated,
        getUserRole: state => state.user.role,
        getUserName: state => state.user.name,
    }
}