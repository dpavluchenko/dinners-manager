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
        <v-layout align-center justify-center row fill-height wrap>
            <v-flex v-for="m in menu" :key="m.date" xs10 md6 lg3>
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
                // this.axios.get('/api/orders/list/').then((response) => {
                //     this.menu = response.data
                //     this.calculateOrdersCount()
                // }).catch((error) => {
                //     handleError(this, error.response)
                // })
                this.menu = [
                    {
                        menuId: 179,
                        day: 'Sat',
                        date: '02.02.2019',
                        orderDetails: [
                            {
                                orderId: 1056,
                                mealId: 1946,
                                name: 'Soup',
                                type: 'FIRST_DISH',
                                chosen: false
                            },
                            {
                                orderId: 1057,
                                mealId: 1947,
                                name: 'Pasta',
                                type: 'GARNISH',
                                chosen: true
                            },
                            {
                                orderId: 1058,
                                mealId: 1948,
                                name: 'Beef',
                                type: 'MEAT',
                                chosen: true
                            },
                            {
                                orderId: 1059,
                                mealId: 1949,
                                name: 'Sour Cake',
                                type: 'DESSERT',
                                chosen: true
                            }
                        ]
                    },
                    {
                        menuId: 180,
                        day: 'Sun',
                        date: '03.02.2019',
                        orderDetails: [
                            {
                                orderId: -1,
                                mealId: 1950,
                                name: 'Soup1',
                                type: 'FIRST_DISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1951,
                                name: 'Pasta1',
                                type: 'GARNISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1952,
                                name: 'Beef1',
                                type: 'MEAT',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1953,
                                name: 'Sour Cake1',
                                type: 'DESSERT',
                                chosen: false
                            }
                        ]
                    },
                    {
                        menuId: 181,
                        day: 'Mon',
                        date: '04.02.2019',
                        orderDetails: [
                            {
                                orderId: -1,
                                mealId: 1954,
                                name: 'Soup2',
                                type: 'FIRST_DISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1955,
                                name: 'Pasta3',
                                type: 'GARNISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1956,
                                name: 'Beef4',
                                type: 'MEAT',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1957,
                                name: 'Sour Cake5',
                                type: 'DESSERT',
                                chosen: false
                            }
                        ]
                    },
                    {
                        menuId: 182,
                        day: 'Tue',
                        date: '05.02.2019',
                        orderDetails: [
                            {
                                orderId: -1,
                                mealId: 1958,
                                name: 'Soup3343',
                                type: 'FIRST_DISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1959,
                                name: 'Pasta34',
                                type: 'GARNISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1960,
                                name: 'Beef434',
                                type: 'MEAT',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1961,
                                name: 'Sour Cake534',
                                type: 'DESSERT',
                                chosen: false
                            }
                        ]
                    },
                    {
                        menuId: 181,
                        day: 'Wed',
                        date: '06.02.2019',
                        orderDetails: [
                            {
                                orderId: -1,
                                mealId: 1954,
                                name: 'Soup2',
                                type: 'FIRST_DISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1955,
                                name: 'Pasta3',
                                type: 'GARNISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1956,
                                name: 'Beef4',
                                type: 'MEAT',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1957,
                                name: 'Sour Cake5',
                                type: 'DESSERT',
                                chosen: false
                            }
                        ]
                    },
                    {
                        menuId: 182,
                        day: 'Thu',
                        date: '07.02.2019',
                        orderDetails: [
                            {
                                orderId: -1,
                                mealId: 1958,
                                name: 'Soup3343',
                                type: 'FIRST_DISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1959,
                                name: 'Pasta34',
                                type: 'GARNISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1960,
                                name: 'Beef434',
                                type: 'MEAT',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1961,
                                name: 'Sour Cake534',
                                type: 'DESSERT',
                                chosen: false
                            }
                        ]
                    },
                    {
                        menuId: 182,
                        day: 'Fri',
                        date: '08.02.2019',
                        orderDetails: [
                            {
                                orderId: -1,
                                mealId: 1958,
                                name: 'Soup3343',
                                type: 'FIRST_DISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1959,
                                name: 'Pasta34',
                                type: 'GARNISH',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1960,
                                name: 'Beef434',
                                type: 'MEAT',
                                chosen: false
                            },
                            {
                                orderId: -1,
                                mealId: 1961,
                                name: 'Sour Cake534',
                                type: 'DESSERT',
                                chosen: false
                            }
                        ]
                    }
                ]
            },
            resetState() {
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