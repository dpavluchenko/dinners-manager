<template>
    <v-container fluid fill-height>
        <v-layout align-center justify-center>
            <v-flex xs12 sm8 md4>
                <v-card dark class="rounded-card elevation-10">

                    <v-form class="mx-4 my-4"
                            ref="form"
                            v-model="valid"
                            lazy-validation
                    >
                        <v-text-field
                                v-model="username"
                                prepend-icon="fingerprint"
                                :counter="15"
                                :rules="authRules"
                                label="Имя пользователя"
                                required
                        ></v-text-field>

                        <v-text-field
                                :counter="15"
                                :rules="authRules"
                                prepend-icon="lock"
                                v-model="password"
                                label="Пароль"
                                type="password">
                        </v-text-field>


                        <v-text-field
                                v-model="firstName"
                                prepend-icon="spellcheck"
                                :counter="15"
                                :rules="nameRules"
                                label="Ваше имя"
                                required
                        ></v-text-field>

                        <v-text-field
                                v-model="surname"
                                prepend-icon="spellcheck"
                                :counter="15"
                                :rules="nameRules"
                                label="Ваша фамилия"
                                required
                        ></v-text-field>

                        <v-select
                                v-model="select"
                                prepend-icon="help"
                                :items="goalTitles"
                                :rules="[v => !!v || 'Сделайте выбор!']"
                                label="С какой целью будете использовать приложение?"
                                required
                        ></v-select>
                        <v-container>
                            <v-btn
                                    round
                                    block
                                    :disabled="!valid"
                                    color="success"
                                    @click="signUp"
                            >
                                Зарегистрироватся
                            </v-btn>

                            <v-btn
                                    round
                                    block
                                    color="error"
                                    @click="reset"
                            >
                                Сбросить значения
                            </v-btn>
                        </v-container>
                    </v-form>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import {handleError} from '../helpers/errorHandler'

    export default {
        name: 'SignUp',
        data: () => ({
            valid: true,
            username: '',
            firstName: '',
            surname: '',
            password: '',
            authRules: [
                v => !!v || 'Заполните, пожалуйста, это поле',
                v => /[a-zA-Z0-9\-_]$/.test(v) || 'Допускаются только латинские символы и цифры без пробелов',
                v => (v && v.length <= 15 && v.length >= 6) || 'Значение должно быть не меньше 6 и не больше 15 символов'
            ],
            nameRules: [
                v => !!v || 'Заполните, пожалуйста, это поле',
                v => (v && v.length <= 15) || 'Значение должно быть не больше 15 символов'
            ],
            select: null,
            goals: [
                {
                    title: 'Просто для заказа обедов',
                    role: 'USER'
                },
                {
                    title: 'Для управления заказами',
                    role: 'MANAGER'
                }
            ]
        }),

        computed: {
            goalTitles: function () {
                return this.goals.map(value => value.title)
            },
            selectedRole: function () {
                return this.select ? this.goals.find(value => value.title === this.select).role : null
            },
            fullName: function () {
                return this.surname.concat(' ', this.firstName)
            }
        },
        methods: {
            reset() {
                this.$refs.form.reset()
            },
            signUp() {
                this.$store.dispatch('signUp', {
                    "username": this.username,
                    "password": this.password,
                    "role": this.selectedRole,
                    "fullName": this.fullName
                }).then(() => {
                    this.$notify({type: 'success', text: 'Вы успешно зарегистрировались!'})
                    this.$router.push('/menu')
                }).catch((e) => {
                    handleError(this, e.response)
                })
            }
        }
    }
</script>

<style scoped>
    .rounded-card {
        border-radius: 10px;
    }
</style>