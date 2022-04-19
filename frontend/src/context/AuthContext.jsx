import React, { createContext, useContext, useEffect } from 'react'
import LoadingCard from '../components/LoadingCard'

import { ModelContext } from './ModelContext'

// TODO : check if user is logged in

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
    // const { openModel, closeModel } = useContext(ModelContext)

    // useEffect(() => {
    //     openModel(<LoadingCard />)

    //     setTimeout(() => closeModel(), 1000)
    // }, [])

    return (
        <AuthContext.Provider value={{}}>
            {children}
        </AuthContext.Provider>
    )
}