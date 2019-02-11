<template>
    <v-layout row justify-center>
        <v-tooltip top>
            <v-btn @click="open" slot="activator" color="error" fab dark flat small>
                <v-icon dark>remove</v-icon>
            </v-btn>
            <span>Удалить</span>
        </v-tooltip>
        <v-dialog lazy v-model="dialog" persistent max-width="290">
            <v-card>
                <v-card-title class="headline">Удалить пользователя?</v-card-title>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" flat @click="cancel">Нет</v-btn>
                    <v-btn color="green" flat @click="remove">Да</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    import {handleError} from '../../../helpers/errorHandler'

    export default {
        props: {
            id: { required: true},
        },
        data() {
            return {
                dialog: false
            }
        },
        methods: {
            cancel() {
                this.dialog = false
            },
            open() {
                this.dialog = true
            },
            remove() {
                this.axios.delete('/api/manage/users?userId='.concat(this.id))
                    .then(() => {
                        this.$emit('deleteUser', this.id)
                        this.cancel()
                    })
                    .catch(error => {
                        handleError(this, error.response)
                        this.cancel()
                    })
            }
        }
    }
</script>

<style scoped>

</style>