import React from 'react'
import { Route, Routes } from 'react-router'

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

import '@Styles/App.css'
import UserProfilePage from './pages/UserProfile.page'
import DiscoverPage from './pages/Discover.page'

function App() {
	return (
		<div className='App'>
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
		</div>
	)
}

export default App
