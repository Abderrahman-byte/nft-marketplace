import React, { useEffect, useState } from 'react'
import { Route, Routes, useLocation } from 'react-router'

import LoginPage from '@Pages/Login.page'
import CreatePages from '@Pages/Upload.pages'
import RegisterPage from '@Pages/Register.page'
import AppHeader from '@Components/AppHeader'
import AppFooter from '@Components/AppFooter'
import AuthenticationOnly from '@Components/AuthenticatedOnly'
import ProfilePages from '@Pages/Profile.pages'
import UserProfilePage from '@Pages/UserProfile.page'
import DiscoverPage from '@Pages/Discover.page'
import MainPage from '@Pages/Main.page'
import DetailsPage from '@Pages/Details.page'
import { NotificationsProvider } from '@Context/NotifiactionsContext'

import '@Styles/App.css'

const App = () => {
	const location = useLocation()

	const [pathname, setPathname] = useState(() => {
		if (location && location.pathname) return location.pathname
		
		return '/'
	})	

	useEffect(() => {
		if (location && location.pathname && location.pathname !== pathname) {
			setPathname(location.pathname)
			window.scroll(0, 0)
		} 	
	}, [location])

	return (
		<div className='App'>
			<NotificationsProvider>
				<AppHeader />
				<Routes>
					<Route index element={<MainPage />} />
					<Route path='/sign-up' element={<RegisterPage />} />
					<Route path='/sign-in' element={<LoginPage />} />
					<Route path='/discover' element={<DiscoverPage />} />
					
					<Route path='/profile/*' element={<AuthenticationOnly><ProfilePages /></AuthenticationOnly>} />
					<Route path='/upload/*' element={<AuthenticationOnly><CreatePages /></AuthenticationOnly>} />
					<Route path='/user/:id/*' element={<UserProfilePage />} />
					<Route path='/details/:id' element={<DetailsPage />} />

				</Routes>
			
				<AppFooter />
			</NotificationsProvider>
		</div>
	)
}

export default App
