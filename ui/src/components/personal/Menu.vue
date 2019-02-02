<template>
    <v-container grid-list-md text-xs-center>
        <v-layout row wrap>
            <v-flex xs2>
                <create-modal @addMenu="addNewMenu"></create-modal>
            </v-flex>
            <v-flex xs9>
                <search-menu @foundMenu="showFoundMenus"></search-menu>
            </v-flex>
        </v-layout>
        <v-layout row justify-center>
            <v-flex xs12>
                <v-data-table hide-actions
                              disable-initial-sort
                              no-data-text="К сожалению, данных нет"
                              :headers="tableHeaders"
                              :items="tableData"
                              item-key="value"
                >
                    <template slot="items" slot-scope="props">
                        <tr>
                            <td class="text-xs-left">{{ props.item.date}}</td>
                            <td class="text-xs-center">
                                {{ props.item.meals.FIRST_DISH}}
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.meals.GARNISH}}
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.meals.MEAT}}
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.meals.DESSERT}}
                            </td>
                            <td class="text-xs-center">
                                <v-layout align-center justify-center row fill-height>
                                    <v-flex xs2>
                                        <update-modal
                                                @updateMenu="updateMenu"
                                                :menu-id="props.item.menuId"
                                                :meals="props.item.meals"
                                                :date="props.item.date"
                                        ></update-modal>
                                    </v-flex>
                                    <v-flex xs2>
                                        <delete-confirm
                                                @deleteMenu="removeMenu"
                                                :id="props.item.menuId">
                                        </delete-confirm>
                                    </v-flex>
                                </v-layout>
                            </td>
                        </tr>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import CreateModal from '../modal/menu/CreateModal'
    import UpdateModal from '../modal/menu/UpdateModal'
    import DeleteConfirm from '../modal/menu/DeleteConfirm'
    import SearchMenu from '../SearchMenu'
    import {types} from '../../helpers/menuTypes'
    import {handleError} from '../../helpers/errorHandler'

    export default {
        name: "Menu",
        components: {CreateModal, SearchMenu, DeleteConfirm, UpdateModal},
        data() {
            return {
                tableHeaders: [],
                tableData: []
            }

        },
        computed: {},
        methods: {
            initHeaders() {
                this.tableHeaders.push({
                    text: 'Дата',
                    align: 'left',
                    sortable: false,
                    value: 'date'
                })
                for (let type in types) {
                    this.tableHeaders.push({
                        text: types[type],
                        align: 'center',
                        sortable: false,
                        value: type
                    })
                }
                this.tableHeaders.push({
                    text: 'Действия',
                    align: 'center',
                    sortable: false,
                    value: 'actions'
                })
            },
            getMenuList() {
                this.axios.get('/api/manage/menu')
                    .then((response) => {
                        this.tableData = response.data
                    })
                    .catch((error) => {
                        handleError(this, error.response)
                    })
            },
            showFoundMenus(data) {
                this.tableData = data
            },
            addNewMenu(data) {
                this.tableData.push(data)
                this.tableData.sort((a, b) => a.date.localeCompare(b.date))
            },
            updateMenu(data) {
                this.tableData.find(value => {
                    return value.menuId === data.menuId
                })
                    .meals = data.meals
            },
            removeMenu(id) {
                this.tableData = this.tableData.filter(value => {
                    return value.menuId !== id
                })
            }
        },
        beforeMount() {
            this.initHeaders()
            this.getMenuList()
        }
    }
</script>

<style scoped>

</style>