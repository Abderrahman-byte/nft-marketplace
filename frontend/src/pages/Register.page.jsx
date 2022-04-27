import React, { useContext, useState } from 'react'

import RegisterForm from '@Components/RegisterForm'
import LoadingCard from '@Components/LoadingCard'
import RegisterSuccessPage from '@Pages/RegisterSuccess.page'
import { DEFAULT_ERROR, translateError } from '@Utils/generic'
import { AuthContext } from '@Context/AuthContext'
import { register } from '@Utils/api'

import '@Styles/RegisterPage.css'
import '@Styles/forms.css'

const RegisterPage = () => {
    const { openModel, closeModel, setAuth } = useContext(AuthContext)
    const [isDone, setDone] = useState(false)

    const submitCallback = async (data, setErrors) => {
        openModel(<LoadingCard />)

        const [done, error] = await register(data)

        if (error && typeof setErrors === 'function') {
            setErrors(translateError(error))
        } else if (typeof setErrors === 'function') {
            setErrors([ DEFAULT_ERROR ])
        }

        setAuth(done)
        setDone(done)
        closeModel()
    }

    if (isDone) return <RegisterSuccessPage />

    return (
        <div className='RegisterPage center-container page'>
            <div className='container card'>
                <h2>Create New Account</h2>
                <RegisterForm submitCallback={submitCallback} />
            </div>
        </div>
    )
}

export default RegisterPage