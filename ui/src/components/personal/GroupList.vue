<template>
    <v-container grid-list-md text-xs-center>
        <v-layout row wrap>
            <v-flex xs2>
                <create-group-modal @addGroup="add"></create-group-modal>
            </v-flex>
            <v-spacer></v-spacer>
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
                            <td class="text-xs-left">{{ props.item.name}}
                                <span v-if="props.item.default">(по умолчанию)</span>
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.dinnerTime}}
                            </td>
                            <td class="text-xs-center">
                                <v-layout align-center justify-center row fill-height>
                                    <v-flex xs1>
                                        <set-default-group
                                                v-if="!props.item.default"
                                                @setDefaultGroup="setDefault"
                                                :id="props.item.id"
                                        ></set-default-group>
                                    </v-flex>
                                    <v-flex xs1>
                                        <update-group-modal
                                                @updateGroup="update"
                                                :id="props.item.id"
                                                :name="props.item.name"
                                                :dinner-time="props.item.dinnerTime"
                                        ></update-group-modal>
                                    </v-flex>
                                    <v-flex xs1>
                                        <delete-group-confirm
                                                @deleteGroup="remove"
                                                :id="props.item.id">
                                        </delete-group-confirm>
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
    import CreateGroupModal from '../modal/group/CreateGroupModal'
    import UpdateGroupModal from '../modal/group/UpdateGroupModal'
    import DeleteGroupConfirm from '../modal/group/DeleteGroupConfirm'
    import SetDefaultGroup from '../modal/group/SetDefaultGroup'
    import {handleError} from '../../helpers/errorHandler'

    export default {
        data() {
            return {
                tableHeaders: [],
                tableData: []
            }
        },
        components: {
            CreateGroupModal, UpdateGroupModal,
            DeleteGroupConfirm, SetDefaultGroup
        },
        methods: {
            initHeaders() {
                this.tableHeaders.push({
                    text: 'Имя группы',
                    align: 'left',
                    sortable: false,
                    value: 'name'
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
            getGroups() {
                this.axios.get('/api/manage/groups').then((response) => {
                    this.tableData = response.data
                })
                    .catch(error => {
                        handleError(this, error.response)
                    })
            },
            add(data) {
                this.tableData.push(data)
            },
            update(data) {
                let group = this.tableData.find(value => value.id === data.groupId);
                group.name = data.name
                group.dinnerTime = data.dinnerTime
            },
            remove(id) {
                this.tableData = this.tableData.filter(value => {
                    return value.id !== id
                })
            },
            setDefault(id) {
                let defaultGroup = this.tableData.find(value => value.default === true)
                let group = this.tableData.find(value => value.id === id);
                if (defaultGroup) defaultGroup.default = false
                group.default = true
            }
        },
        mounted() {
            this.initHeaders()
            this.getGroups()
        }
    }
</script>

<style scoped>

</style>