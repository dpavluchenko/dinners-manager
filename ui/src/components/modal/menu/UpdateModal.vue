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
                    <span class="headline">Изменить меню</span>
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
                                    <v-text-field
                                            v-model="date"
                                            prepend-icon="event"
                                            label="Дата"
                                            readonly
                                    ></v-text-field>
                                    <v-text-field
                                            v-model="changedMeals.FIRST_DISH"
                                            prepend-icon="text_format"
                                            :rules="rules"
                                            :label="getLabel('FIRST_DISH')"
                                            required
                                    ></v-text-field>

                                    <v-text-field
                                            :rules="rules"
                                            prepend-icon="text_format"
                                            v-model="changedMeals.GARNISH"
                                            :label="getLabel('GARNISH')">
                                    </v-text-field>
                                    <v-text-field
                                            :rules="rules"
                                            prepend-icon="text_format"
                                            v-model="changedMeals.MEAT"
                                            :label="getLabel('MEAT')">
                                    </v-text-field>
                                    <v-text-field
                                            :rules="rules"
                                            prepend-icon="text_format"
                                            v-model="changedMeals.DESSERT"
                                            :label="getLabel('DESSERT')">
                                    </v-text-field>

                                </v-form>
                            </v-flex>
                        </v-layout>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" flat @click="cancel">Отмена</v-btn>
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
        name: "UpdateModal",
        props: {
            menuId: null,
            date: null,
            meals: {}
        },
        data() {
            return {
                changedMeals: {
                    FIRST_DISH: '',
                    GARNISH: '',
                    MEAT: '',
                    DESSERT: ''
                },
                dialog: false,
                valid: true,
                rules: [
                    v => !!v || 'Заполните, пожалуйста, это поле',
                ]
            }
        },
        methods: {
            getLabel(type) {
                return types[type]
            },
            save() {
                if (this.$refs.form.validate()) {
                    let data = {
                        menuId: this.menuId,
                        meals: this.changedMeals
                    }
                    this.axios.put('/api/manage/menu', data)
                        .then(() => {
                            this.$emit('updateMenu', data)
                            this.close()
                        }).catch(error => {
                        handleError(this, error.response)
                        this.cancel()
                    })
                }
            },
            open() {
                this.copyMeals()
                this.dialog = true
            },
            close() {
                this.dialog = false
            },
            cancel(){
                this.copyMeals()
                this.close()
            },
            copyMeals(){
                this.changedMeals = Object.assign({}, this.meals)
            }
        }
    }
</script>

<style scoped>

</style>