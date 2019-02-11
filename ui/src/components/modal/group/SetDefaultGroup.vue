<template>
    <v-layout row justify-center>
        <v-tooltip top>
            <v-btn @click="open" slot="activator" color="orange darken-3" fab dark flat small>
                <v-icon dark>favorite</v-icon>
            </v-btn>
            <span>Установить по умолчанию</span>
        </v-tooltip>
        <v-dialog lazy v-model="dialog" persistent max-width="290">
            <v-card>
                <v-card-title class="headline">Установить по умолчанию?</v-card-title>
                <v-card-text>Всем новые пользователи будут добавлятся в эту группу</v-card-text>
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
            id: null
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
                this.axios.put('/api/manage/groups/default?groupId='.concat(this.id))
                    .then(() => {
                        this.$emit('setDefaultGroup', this.id)
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