<template>
    <v-layout row justify-center>
        <v-tooltip top>
            <v-btn @click="open" slot="activator" fab dark large color="green">
                <v-icon dark>add</v-icon>
            </v-btn>
            <span>Добавить</span>
        </v-tooltip>
        <v-dialog lazy v-model="dialog" persistent max-width="600px">
            <v-card>
                <v-card-title>
                    <span class="headline">Добавить меню</span>
                </v-card-title>
                <v-card-text>
                    <v-container grid-list-md>
                        <v-layout wrap>
                            <v-flex>
                                <v-form class="mx-4 my-4"
                                        ref="form"
                                        v-model="valid"
                                        lazy-validation
                                >
                                    <v-menu
                                            :close-on-content-click="false"
                                            v-model="picker"
                                            :nudge-right="40"
                                            lazy
                                            transition="scale-transition"
                                            offset-y
                                            full-width
                                            min-width="290px"
                                    >
                                        <v-text-field
                                                slot="activator"
                                                v-model="menu.date"
                                                :rules="rules"
                                                label="Выберите дату"
                                                prepend-icon="event"
                                                readonly
                                        ></v-text-field>
                                        <v-date-picker v-model="date" @input="picker = false"></v-date-picker>
                                    </v-menu>
                                    <v-text-field
                                            v-model="menu.meals.FIRST_DISH"
                                            prepend-icon="text_format"
                                            :rules="rules"
                                            :label="getLabel('FIRST_DISH')"
                                            required
                                    ></v-text-field>

                                    <v-text-field
                                            :rules="rules"
                                            prepend-icon="text_format"
                                            v-model="menu.meals.GARNISH"
                                            :label="getLabel('GARNISH')">
                                    </v-text-field>
                                    <v-text-field
                                            :rules="rules"
                                            prepend-icon="text_format"
                                            v-model="menu.meals.MEAT"
                                            :label="getLabel('MEAT')">
                                    </v-text-field>
                                    <v-text-field
                                            :rules="rules"
                                            prepend-icon="text_format"
                                            v-model="menu.meals.DESSERT"
                                            :label="getLabel('DESSERT')">
                                    </v-text-field>

                                </v-form>
                            </v-flex>
                        </v-layout>
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
    import {types} from '../../../helpers/menuTypes'
    import {handleError} from '../../../helpers/errorHandler'

    export default {
        name: "CreateModal",
        data() {
            return {
                dialog: false,
                valid: true,
                date: null,
                picker: false,
                menu: {
                    date: null,
                    meals: {
                        FIRST_DISH: '',
                        GARNISH: '',
                        MEAT: '',
                        DESSERT: ''
                    }
                },
                rules: [
                    v => !!v || 'Заполните, пожалуйста, это поле',
                ]
            }
        },
        watch: {
            date(val) {
                this.menu.date = this.formatDate(val)
            }
        },
        methods: {
            formatDate(date) {
                if (!date) return null
                const [year, month, day] = date.split('-')
                return `${day.padStart(2, '0')}.${month.padStart(2, '0')}.${year}`
            },
            open() {
                this.dialog = true
            },
            getLabel(type) {
                return types[type]
            },
            save() {
                if (this.$refs.form.validate()) {
                    this.axios.post('/api/manage/menu', this.menu)
                        .then((response) => {
                            this.$emit('addMenu', response.data)
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
        },
        mounted() {
            this.menu.date = this.formatDate(new Date().toISOString().substr(0, 10))
        }
    }
</script>

<style scoped>

</style>