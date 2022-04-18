import React, { createContext, useContext, useEffect } from 'react'
import LoadingCard from '../components/LoadingCard'

import { ModelContext } from './ModelContext'

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
    const { openModel } = useContext(ModelContext)

    useEffect(() => {
        openModel(<LoadingCard />)
    }, [])

    return (
        <AuthContext.Provider value={{}}>
            {children}
        </AuthContext.Provider>
    )
}