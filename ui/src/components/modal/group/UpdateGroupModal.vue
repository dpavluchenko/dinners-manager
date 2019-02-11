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
                    <span class="headline">Изменить группу</span>
                </v-card-title>
                <v-card-text>
                    <v-container grid-list-md>
                        <v-form class="mx-4 my-4"
                                ref="form"
                                v-model="valid"
                                lazy-validation
                        >
                            <v-layout row wrap>
                                <v-flex>
                                    <v-text-field
                                            v-model="groupName"
                                            prepend-icon="text_format"
                                            :rules="rules"
                                            label="Имя группы"
                                            required
                                    ></v-text-field>
                                </v-flex>
                            </v-layout>
                            <v-layout row wrap>
                                <v-flex xs11 sm6>
                                    <v-menu
                                            ref="menu"
                                            v-model="menu1"
                                            :close-on-content-click="false"
                                            :nudge-right="40"
                                            lazy
                                            transition="scale-transition"
                                            offset-y
                                            full-width
                                            max-width="290px"
                                            min-width="290px"
                                    >
                                        <v-text-field
                                                slot="activator"
                                                :rules="rules"
                                                v-model="fromTime"
                                                label="Начало обеда"
                                                prepend-icon="access_time"
                                                readonly
                                        ></v-text-field>
                                        <v-time-picker
                                                v-if="menu1"
                                                format="24hr"
                                                :max="toTime"
                                                v-model="fromTime"
                                                full-width
                                                @click:minute="$refs.menu.save(fromTime)"
                                        ></v-time-picker>
                                    </v-menu>
                                </v-flex>
                                <v-spacer></v-spacer>
                                <v-flex sm6>
                                    <v-menu
                                            ref="menu"
                                            v-model="menu2"
                                            :close-on-content-click="false"
                                            :nudge-right="40"
                                            lazy
                                            transition="scale-transition"
                                            offset-y
                                            full-width
                                            max-width="290px"
                                            min-width="290px"
                                    >
                                        <v-text-field
                                                slot="activator"
                                                :rules="rules"
                                                v-model="toTime"
                                                label="Окончание обеда"
                                                prepend-icon="access_time"
                                                readonly
                                        ></v-text-field>
                                        <v-time-picker
                                                v-if="menu2"
                                                format="24hr"
                                                :min="fromTime"
                                                v-model="toTime"
                                                full-width
                                                @click:minute="$refs.menu.save(toTime)"
                                        ></v-time-picker>
                                    </v-menu>
                                </v-flex>
                            </v-layout>
                        </v-form>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" flat @click="close">Отмена</v-btn>
                    <v-btn color="success" :disabled="!valid" flat @click="save">Сохранить</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-layout>

</template>

<script>
    import {handleError} from '../../../helpers/errorHandler'

    export default {
        props: {
            id: null,
            name: null,
            dinnerTime: null
        },
        data() {
            return {
                fromTime: null,
                toTime: null,
                groupName: null,
                menu1: false,
                menu2: false,
                dialog: false,
                valid: true,
                rules: [
                    v => !!v || 'Заполните, пожалуйста, это поле',
                ]
            }
        },
        computed: {
            changedDinnerTime() {
                return this.fromTime.concat('-', this.toTime)
            }
        },
        methods: {
            save() {
                if (this.$refs.form.validate()) {
                    let data = {
                        groupId: this.id,
                        name: this.groupName,
                        dinnerTime: this.changedDinnerTime
                    }
                    this.axios.put('/api/manage/groups/change', data)
                        .then(() => {
                            this.$emit('updateGroup', data)
                            this.close()
                        }).catch(error => {
                        handleError(this, error.response)
                        this.close()
                    })
                }
            },
            open() {
                this.initValues()
                this.dialog = true
            },
            close() {
                this.dialog = false
                this.$refs.form.reset()
            },
            initValues() {
                this.groupName = this.name
                let times = this.dinnerTime.split('-');
                this.fromTime = times[0]
                this.toTime = times[1]
            }
        }
    }
</script>

<style scoped>

</style>