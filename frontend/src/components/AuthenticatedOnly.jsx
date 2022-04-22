import React, { useContext } from 'react'
import { Navigate, useLocation } from 'react-router'
import { AuthContext } from '../context/AuthContext'

const AuthenticationOnly = ({ children }) => {
    const { authenticated } = useContext(AuthContext)
    const location = useLocation()

    if (authenticated === undefined) return <></>

    if (!authenticated)
		return (
			<Navigate state={{ next: location.pathname }}
				to={{
					pathname: '/sign-in',
				}}
			></Navigate>
		)

    return <>{children}</>
}

export default AuthenticationOnly