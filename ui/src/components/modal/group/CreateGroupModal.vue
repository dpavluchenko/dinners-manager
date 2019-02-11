<template>
    <v-layout row justify-center>
        <v-tooltip top>
            <v-btn @click="open" slot="activator" fab dark large color="green">
                <v-icon dark>add</v-icon>
            </v-btn>
            <span>Создать</span>
        </v-tooltip>
        <v-dialog lazy v-model="dialog" persistent max-width="600px">
            <v-card>
                <v-card-title>
                    <span class="headline">Создать новую группу</span>
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
                    <v-btn color="blue darken-1" flat @click="reset">Отмена</v-btn>
                    <v-btn color="success" :disabled="!valid" flat @click="save">Сохранить</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    import {handleError} from '../../../helpers/errorHandler'

    export default {
        data() {
            return {
                dialog: false,
                valid: true,
                fromTime: null,
                toTime: null,
                groupName: null,
                menu1: false,
                menu2: false,
                rules: [
                    v => !!v || 'Заполните, пожалуйста, это поле',
                ]
            }
        },
        computed: {
            dinnerTime() {
                return this.fromTime.concat('-', this.toTime)
            }
        },
        watch: {},
        methods: {
            open() {
                this.dialog = true
            },
            save() {
                if (this.$refs.form.validate()) {
                    this.axios.post('/api/manage/groups', {
                        name: this.groupName,
                        dinnerTime: this.dinnerTime
                    })
                        .then((response) => {
                            this.$emit('addGroup', response.data)
                            this.reset()
                        }).catch(error => {
                        handleError(this, error.response)
                        this.reset()
                    })
                }
            },
            reset() {
                this.dialog = false
                this.$refs.form.reset()
            },
        }
    }
</script>

<style scoped>

</style>