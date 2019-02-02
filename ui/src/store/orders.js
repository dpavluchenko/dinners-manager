import Vue from "vue";

export default {
    state: {
        orders: {
            save: false,
            newOrders: []
        }
    },
    mutations: {
        setOrderSave(state) {
            state.orders.save = true
        },
        resetOrderSave(state) {
            state.orders.save = false
        },
        addNewOrders(state, payload) {
            state.orders.newOrders = payload
        },
        clearNewOrders(state) {
            state.orders.newOrders = []
        }
    },
    actions: {
        saveOrders(context, orders) {
            context.commit('clearNewOrders')
            return new Promise((resolve, reject) => {
                const options = {
                    method: 'POST',
                    data: orders,
                    url: '/api/orders/'
                }
                Vue.axios(options).then((response) => {
                    context.commit('addNewOrders', response.data)
                    context.commit('resetOrderSave')
                    resolve()
                }).catch((error) => {
                    context.commit('resetOrderSave')
                    reject(error)
                })
            })
        },
    },
    getters: {
        isOrderSave: state => state.orders.save,
        getNewOrders: state => state.orders.newOrders
    }
}