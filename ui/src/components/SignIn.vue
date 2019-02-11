<template>
    <v-container fluid>
        <v-layout align-center justify-center row>
            <v-flex xs12 md6 lg4>
                <v-card class="rounded-card elevation-10" dark>
                    <v-card-media>
                        <v-img  max-height="240px"
                                contain
                                :src="require('.././assets/broccoli2.png')"
                                aspect-ratio="1"
                                class="grey lighten-2 hidden-sm-and-down"
                        >
                        </v-img>
                    </v-card-media>
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

                        <v-container>
                            <v-btn round
                                   block
                                   :disabled="!valid"
                                   color="success"
                                   @click="signIn"
                            >
                                Войти
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
        name: 'SignIn',
        data: () => ({
            valid: true,
            username: '',
            password: '',
            authRules: [
                v => !!v || 'Заполните, пожалуйста, это поле',
                v => /[a-zA-Z0-9\-_]$/.test(v) || 'Допускаются только латинские символы и цифры  без пробелов',
                v => (v && v.length <= 15 && v.length >= 6) || 'Значение должно быть не меньше 6 и не больше 15 символов'
            ]
        }),

        methods: {
            reset() {
                this.$refs.form.reset()
            },
            signIn() {
                this.$store.dispatch('signIn', {
                    "username": this.username,
                    "password": this.password
                }).then(() => {
                    this.$notify({type: 'success', text: 'Вы успешно вошли!'})
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