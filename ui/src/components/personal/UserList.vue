<template>
    <v-container fluid>
        <v-layout row>
            <v-flex xs12>
                <v-card>
                    <v-card-title>
                        <v-spacer></v-spacer>
                        <v-btn @click="show" class="mt-4" dark small flat color="green darken-1">
                            Показать
                            <v-icon dark>search</v-icon>
                        </v-btn>
                        <v-text-field
                                v-model="searchCriteria"
                                @keyup.enter="show"
                                clearable
                                label="Поиск по ФИО"
                                single-line
                                hide-details
                        ></v-text-field>
                    </v-card-title>
                    <v-data-table
                            hide-actions
                            disable-initial-sort
                            no-data-text="К сожалению, данных нет"
                            :headers="tableHeaders"
                            :items="tableData"
                            item-key="value"
                    >
                        <template slot="items" slot-scope="props">
                            <td class="text-xs-left">{{ props.item.fullName }}</td>
                            <td class="text-xs-center">{{ props.item.groupName==='no assigned' ?
                                'Не назначено': props.item.groupName }}
                            </td>
                            <td class="text-xs-center">{{ props.item.dinnerTime }}</td>
                            <td class="text-xs-center">
                                <v-layout align-center justify-center row fill-height>
                                    <v-flex xs2>
                                        <update-user-modal
                                                @updateUser="update"
                                                :user-id="props.item.userId"
                                                :group-name="props.item.groupName"
                                                :dinner-time="props.item.dinnerTime"
                                        ></update-user-modal>
                                    </v-flex>
                                    <v-flex xs2>
                                        <delete-user-confirm
                                                @deleteUser="remove"
                                                :id="props.item.userId"
                                        ></delete-user-confirm>
                                    </v-flex>
                                </v-layout>
                            </td>
                        </template>
                    </v-data-table>
                </v-card>
            </v-flex>
        </v-layout>
        <v-layout align-end justify-space-around row fill-height>
            <v-spacer shrink pa-1></v-spacer>
            <v-flex grow pa-1 xs7 lg4>
                <v-layout align-center justify-center row fill-height>
                    <v-flex xs1>
                        <v-select
                                :items="itemsPerPage"
                                v-model="pageSize"
                                label="Разм."
                        ></v-select>
                    </v-flex>
                    <v-flex xs11>
                        <v-pagination
                                v-model="pageNumber"
                                :length="totalPages"
                        ></v-pagination>
                    </v-flex>
                </v-layout>
            </v-flex>
            <v-spacer shrink pa-1></v-spacer>
        </v-layout>
    </v-container>
</template>

<script>
    import UpdateUserModal from '../modal/user/UpdateUserModal'
    import DeleteUserConfirm from '../modal/user/DeleteUserConfirm'
    import {handleError} from '../../helpers/errorHandler'

    export default {
        name: "UserList",
        components: {UpdateUserModal, DeleteUserConfirm},
        data() {
            return {
                pageNumber: 1,
                totalPages: 10,
                searchCriteria: null,
                pageSize: 10,
                itemsPerPage: [5, 10, 15, 20],
                tableHeaders: [],
                tableData: []
            }
        },
        methods: {
            initHeaders() {
                this.tableHeaders.push({
                    text: 'ФИО',
                    align: 'left',
                    sortable: false,
                    value: 'fullName'
                })
                this.tableHeaders.push({
                    text: 'Группа',
                    align: 'center',
                    sortable: false,
                    value: 'groupName'
                })
                this.tableHeaders.push({
                    text: 'Время обеда',
                    align: 'center',
                    sortable: false,
                    value: 'dinnerTime'
                })
                this.tableHeaders.push({
                    text: 'Действия',
                    align: 'center',
                    sortable: false,
                    value: 'actions'
                })
            },
            initData(data) {
                this.totalPages = data.totalPages
                this.tableData = data.items
            },
            show() {
                if (this.searchCriteria) {
                    this.pageNumber = 1
                    this.search(this.searchCriteria)
                } else {
                    this.getUsers()
                }
            },
            search(fullName) {
                this.axios.get('/api/manage/users/search', {
                    params: {
                        fullName: fullName,
                        page: this.pageNumber,
                        size: this.pageSize
                    }
                }).then((response) => {
                    this.initData(response.data)
                })
                    .catch(error => {
                        handleError(this, error.response)
                    })
            },
            getUsers() {
                this.axios.get('/api/manage/users/list', {
                    params: {
                        page: this.pageNumber,
                        size: this.pageSize
                    }
                }).then((response) => {
                    this.initData(response.data)
                })
                    .catch(error => {
                        handleError(this, error.response)
                    })
            },
            update(data) {
                let user = this.tableData.find(value => value.userId === data.userId);
                user.groupName = data.groupName
                user.dinnerTime = data.dinnerTime
            },
            remove(id) {
                this.tableData = this.tableData.filter(value => {
                    return value.userId !== id
                })
            }
        },
        watch: {
            pageNumber: function () {
                this.show()
            },
            pageSize: function () {
                this.pageNumber = 1
                this.show()
            }
        },
        mounted() {
            this.initHeaders()
            this.getUsers()
        }
    }
</script>

<style scoped>

</style>