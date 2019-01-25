import qs from 'qs'
import Vue from 'vue'

export default {
    state: {
        isAuthenticated: null,
        role: null,
        name: null
    },
    mutations: {
        setUserParams (state, payload) {
            state.isAuthenticated = true
            state.role = payload.role
            state.name = payload.name
        },
        resetUserParams (state) {
            state.isAuthenticated = null
            state.role = null
            state.name = null
        }
    },
    actions: {
        signIn(context, userData){
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
                        console.log(error)
                        reject(error)
                    })
            })
        },
        signUp (context, userData) {
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
                    console.log(error)
                    reject(error)
                })
            })
        },
        signOut ({commit}) {
            return new Promise((resolve, reject) => {
                Vue.axios({url: '/api/logout'}).then((response) => {
                    resolve(response)
                    commit('resetUserParams')
                }).catch((error) => reject(error))
            })
        },
        checkAuth (context) {
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
        isAuthenticated: state => state.isAuthenticated,
        getUserRole: state => state.role,
        getUserName: state => state.name
    }
}