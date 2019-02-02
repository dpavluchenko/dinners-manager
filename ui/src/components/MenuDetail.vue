<template>
    <div>
        <v-tooltip top>
            <v-btn slot="activator" fab dark small color="blue accent-3"
                   @click="open"
            >
                <v-icon dark>description</v-icon>
            </v-btn>
            <span>Подробнее</span>
        </v-tooltip>
        <v-layout row justify-center>
            <v-dialog lazy v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
                <v-card>
                    <v-toolbar dark color="primary">
                        <v-btn icon dark @click.stop="close">
                            <v-icon>close</v-icon>
                        </v-btn>
                        <v-toolbar-title>Меню вашей группы на {{date}}</v-toolbar-title>
                    </v-toolbar>
                    <v-data-table hide-actions
                                  disable-initial-sort
                                  no-data-text="К сожалению, данных нет, попробуйте зайти позже"
                                  :headers="tableHeaders"
                                  :items="tableData"
                                  item-key="value"
                    >
                        <template slot="items" slot-scope="props">
                            <tr>
                                <td>{{ props.item.fullName}}</td>
                                <td class="text-xs-center">
                                    <v-icon v-if="!props.item.FIRST_DISH">check_box_outline_blank</v-icon>
                                    <v-icon v-if="props.item.FIRST_DISH">check_box</v-icon>
                                </td>
                                <td class="text-xs-center">
                                    <v-icon v-if="!props.item.GARNISH">check_box_outline_blank</v-icon>
                                    <v-icon v-if="props.item.GARNISH">check_box</v-icon>
                                </td>
                                <td class="text-xs-center">
                                    <v-icon v-if="!props.item.MEAT">check_box_outline_blank</v-icon>
                                    <v-icon v-if="props.item.MEAT">check_box</v-icon>
                                </td>
                                <td class="text-xs-center">
                                    <v-icon v-if="!props.item.DESSERT">check_box_outline_blank</v-icon>
                                    <v-icon v-if="props.item.DESSERT">check_box</v-icon>
                                </td>
                            </tr>
                        </template>
                    </v-data-table>
                </v-card>
            </v-dialog>
        </v-layout>
    </div>
</template>

<script>
    import {types} from '../helpers/menuTypes'
    import {handleError} from '../helpers/errorHandler'

    export default {
        props: {
            date: {
                type: String,
                required: true
            }
        },
        data() {
            return {
                dialog: false,
                tableHeaders: [],
                tableData: []
            }
        },
        computed: {
            url() {
                return '/api/orders/details?date='.concat(this.date)
            }
        },
        methods: {
            fillTableData() {
                this.axios.get(this.url).then((response) => {
                    response.data.forEach(value => {
                        Object.assign(value, value.chooseByType)
                        delete value.chooseByType
                        this.tableData.push(value)
                    })
                }).catch(error => handleError(this, error.response))
            },
            open() {
                this.dialog = true
                this.fillTableData()
            },
            close() {
                this.dialog = false
                this.tableData = []
            }
        },
        beforeMount() {
            this.tableHeaders.push({
                text: 'Фамилия',
                align: 'left',
                sortable: false,
                value: 'fullName'
            })
            for (let type in types) {
                this.tableHeaders.push({
                    text: types[type],
                    align: 'center',
                    sortable: false,
                    value: type
                })
            }
        }
    }
</script>

<style scoped>

</style>