import Vue from 'vue'
import Vuex from 'vuex'
import user from './store/user'
import orders from './store/orders'

Vue.use(Vuex)

export default new Vuex.Store({
modules: {
  user,
  orders
}
})
