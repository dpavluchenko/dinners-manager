<template>
    <v-layout row justify-center>
        <v-tooltip top>
            <v-btn @click="open" slot="activator" flat fab dark small color="light-blue darken-3">
                <v-icon dark>edit</v-icon>
            </v-btn>
            <span>Изменить</span>
        </v-tooltip>
        <v-dialog lazy v-model="dialog" persistent max-width="600px">
            <v-card>
                <v-card-title>
                    <span class="headline">Изменить пользователя</span>
                </v-card-title>
                <v-card-text>
                    <v-container grid-list-md>
                        <v-layout wrap>
                            <v-flex xs12 d-flex>
                                <v-form ref="form"
                                        v-model="valid"
                                        lazy-validation>
                                    <v-select
                                            :items="groupNames"
                                            v-model="selectedGroup"
                                            box
                                            no-data-text="Нет ни одной группы"
                                            :rules="rules"
                                            prepend-icon="group"
                                            label="Выберите группу для пользователя"
                                    ></v-select>
                                </v-form>
                            </v-flex>
                        </v-layout>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" flat @click="close">Отмена</v-btn>
                    <v-btn color="success" flat @click="save">Сохранить</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-layout>

</template>

<script>
    import {handleError} from '../../../helpers/errorHandler'

    export default {
        props: {
            userId: {required: true},
            groupName: {required: true},
            dinnerTime: {required: true},
        },
        data() {
            return {
                valid: true,
                dialog: false,
                groups: [],
                groupNames: [],
                selectedGroup: null,
                rules: [
                    v => !!v || 'Выберите, пожалуйста, группу',
                ]
            }
        },
        methods: {
            save() {
                if (this.$refs.form.validate()) {
                    let group = this.findGroup()
                    this.axios.put('/api/manage/users?userId='
                        .concat(this.userId,
                            '&groupId=', group.id))
                        .then(() => {
                            this.$emit('updateUser', {
                                userId: this.userId,
                                groupName: group.name,
                                dinnerTime: group.dinnerTime
                            })
                            this.close()
                        }).catch(error => {
                        handleError(this, error.response)
                        this.close()
                    })
                } else this.snackbar = true
            },
            getAllGroups() {
                this.axios.get('/api/manage/groups').then(response => {
                    this.groups = response.data
                    response.data.forEach(value => {
                        this.groupNames.push(this.joinGroupName(value.name, value.dinnerTime))
                    })
                }).catch(error => handleError(this, error.response))
            },
            open() {
                this.dialog = true
                this.selectedGroup = this.joinGroupName(this.groupName, this.dinnerTime)
                this.getAllGroups()
            },
            close() {
                this.dialog = false
                this.selectedGroup = null
                this.groupNames = []
                this.groups = []
            },
            joinGroupName(groupName, dinnerTime) {
                if (groupName === 'no assigned') {
                    return null
                } else return groupName.concat(', ', dinnerTime)
            },
            findGroup() {
                let name = this.selectedGroup.split(',')[0]
                return this.groups.find(value => value.name === name)
            }
        }
    }
</script>

<style scoped>

</style>