<template>
    <v-container
            hover
            style="max-width: 400px;"
            grid-list-xs
    >
        <v-layout align-space-between justify-space-between column fill-height>
            <v-flex xs12>
                <v-card :color="colors.card" class="black--text rounded-card">
                    <v-layout>
                        <v-flex xs12>
                            <v-card-text>
                                <div>
                                    <div class="headline">{{menuTitle}}</div>

                                    <v-checkbox class="my-1" :color="colors.checkbox"
                                                :label="findMeal('FIRST_DISH')"
                                                v-model="FIRST_DISH">
                                    </v-checkbox>

                                    <v-checkbox :color="colors.checkbox" class="my-1"
                                                :label="findMeal('GARNISH')"
                                                v-model="GARNISH">
                                    </v-checkbox>

                                    <v-checkbox :color="colors.checkbox" class="my-1"
                                                :label="findMeal('MEAT')"
                                                v-model="MEAT">
                                    </v-checkbox>

                                    <v-checkbox :color="colors.checkbox" class="my-0"
                                                :label="findMeal('DESSERT')"
                                                v-model="DESSERT">
                                    </v-checkbox>
                                </div>
                            </v-card-text>
                        </v-flex>
                    </v-layout>
                    <v-card-actions class="text-xs-center">
                        <v-spacer></v-spacer>
                        <v-tooltip top>
                            <v-btn @click="chooseAll" slot="activator" fab dark small color="green darken-1">
                                <v-icon dark>done_all</v-icon>
                            </v-btn>
                            <span>Выбрать все!</span>
                        </v-tooltip>
                        <menu-detail :date="menu.date"></menu-detail>
                    </v-card-actions>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import MenuDetail from './MenuDetail'
    import menuDays from '../helpers/menuDays'

    export default {
        props: {
            menu: {
                date: '',
                day: '',
                orderDetails: []
            }
        },
        data() {
            return {
                colors: {},
                result: {},
                menuDay: '',
                FIRST_DISH: false,
                GARNISH: false,
                MEAT: false,
                DESSERT: false
            }
        },
        components: {MenuDetail},
        computed: {
            isOrderSave() {
                return this.$store.getters.isOrderSave
            },
            hasNewOrders() {
                return this.$store.getters.getNewOrders.length !== 0
            },
            menuTitle() {
                return this.menuDay.concat(', ', this.menu.date)
            }
        },
        watch: {
            isOrderSave: function (val) {
                if (val) {
                    this.emitResult()
                }
            },
            hasNewOrders: function (val) {
                if (val) {
                    this.updateOrdersInfo()
                }
            },
            FIRST_DISH: function (val) {
                this.result['FIRST_DISH'].chosen = val
            },
            GARNISH: function (val) {
                this.result['GARNISH'].chosen = val
            },
            MEAT: function (val) {
                this.result['MEAT'].chosen = val
            },
            DESSERT: function (val) {
                this.result['DESSERT'].chosen = val
            }
        },
        methods: {
            emitResult() {
                this.$emit('result', Object.values(this.result))
            },
            chooseAll() {
                this.FIRST_DISH = true
                this.GARNISH = true
                this.MEAT = true
                this.DESSERT = true
            },
            findMenuProps() {
                let props = menuDays[this.menu.day];
                this.menuDay = props.day
                this.colors = props.colors
            },
            findMeal(type) {
                return this.menu.orderDetails.find(value => value.type === type).name
            },
            initResult() {
                this.menu.orderDetails.forEach(order => {
                    this.$data[order.type] = order.chosen
                    this.result[order.type] = {
                        orderId: order.orderId,
                        mealId: order.mealId,
                        chosen: order.chosen
                    }
                })
            },
            updateOrdersInfo() {
                let newOrders = this.$store.getters.getNewOrders;
                for (let type in this.result) {
                    let oldOrder = this.result[type];
                    let newOrder = newOrders.find(value => value.mealId === oldOrder.mealId);
                    if (newOrder) oldOrder.orderId = newOrder.orderId
                }
            }
        },
        beforeMount() {
            this.findMenuProps()
            this.initResult()
        }
    }
</script>

<style scoped>
    .rounded-card {
        border-radius: 13px;
    }
</style>