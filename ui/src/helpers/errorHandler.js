/* eslint-disable */

export function handleError(vm, error) {

    if (!error) return

    if ((error.status === 502 || error.status === 503)) {
        vm.$notify({
            type:'error',
            text: 'Не удается установить соединение с сервером'
        })
    }

    if (vm.$route.path === '/sign-in' && error.status === 401) {
        vm.$notify({
            type:'error',
            text: 'Не удалось войти. Пожалуйста, проверьте правильность ввода имени пользователя и пароля'
        })

    } else if (error.status === 401) {
        vm.$store.dispatch('signOut').then(() => {
            vm.$router.push('sign-in')
        }).catch(() => {
            console.log('Auth error')
        })
    }

    if (error.status === 500 || error.status === 400 || error.status === 404) {
        vm.$notify({
            type:'error',
            text: error.data
        })
    }

}