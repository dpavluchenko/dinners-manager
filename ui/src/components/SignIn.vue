<template>
    <v-container align-center align-content-center
                 fill-height>
        <v-layout align-center justify-center>
            <v-flex xs12 sm8 md4>
                <v-card class="rounded-card elevation-10" dark>

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
                            <v-btn  round
                                    block
                                    :disabled="!valid"
                                    color="success"
                                    @click="validate"
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
    export default {
        name: 'SignIn',
        data: () => ({
            valid: true,
            username: '',
            password: '',
            authRules: [
                v => !!v || 'Заполните, пожалуйста, это поле',
                v => /[a-zA-Z0-9\-_]$/.test(v) || 'Допускаются только латинские символы без пробелов',
                v => (v && v.length <= 15 && v.length >= 6) || 'Значение должно быть не меньше 6 и не больше 15 символов'
            ]
        }),

        methods: {
            validate () {
                if (this.$refs.form.validate()) {
                    this.snackbar = true
                }
            },
            reset () {
                this.$refs.form.reset()
            }
        }
    }
</script>

<style scoped>
    .rounded-card{
        border-radius:10px;
    }
</style>