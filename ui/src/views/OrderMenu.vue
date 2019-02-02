<template>
    <v-container grid-list-md text-xs-center>
        <v-layout row>
            <v-spacer></v-spacer>
            <v-btn
                    :loading="loading"
                    :disabled="loading"
                    color="blue-grey"
                    class="white--text"
                    @click="publishSaveOrderEvent"
            >
                Сохранить
                <v-icon right dark>cloud_upload</v-icon>
            </v-btn>
        </v-layout>
        <v-layout row wrap>
            <v-flex v-for="m in menu" :key="m.date" xs3>
                <MenuItem @result="addResult" :menu="m"></MenuItem>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import MenuItem from '../components/MenuItem'
    import {handleError} from '../helpers/errorHandler'

    export default {
        components: {MenuItem},
        data() {
            return {
                loading: false,
                orderCount: 0,
                menu: [],
                result: []
            }
        },
        computed: {
            savedResult() {
                return this.result.length
            }
        },
        watch: {
            savedResult: function (val) {
                if (val === this.orderCount) {
                    this.saveOrders()
                }
            }
        },
        methods: {
            addResult(payload) {
                this.result.push.apply(this.result, payload)
            },
            publishSaveOrderEvent() {
                this.$store.commit('setOrderSave')
                this.loading = true
            },
            saveOrders() {
                this.$store.dispatch('saveOrders', this.result).then(() => {
                    this.resetState()
                    this.$notify({
                        type: 'success',
                        text: 'Заказ сохранен! Приятного аппетита :)'
                    })
                }).catch((error) => {
                    this.resetState()
                    handleError(this, error.response)
                })
            },
            calculateOrdersCount() {
                this.menu.forEach(value => {
                    this.orderCount += value.orderDetails.length
                })
            },
            getMenu() {
                this.axios.get('/api/orders/list/').then((response) => {
                    this.menu = response.data
                    this.calculateOrdersCount()
                }).catch((error) => {
                    handleError(this, error.response)
                })
            },
            resetState(){
                this.result = []
                this.loading = false
            }
        },
        beforeMount() {
            this.getMenu()
        }
    }
</script>

<style scoped>

</style>