import React from 'react'
import { Route, Routes } from 'react-router'

import LoginPage from '@Pages/Login.page'
import CreatePages from '@Pages/Upload.pages'
import RegisterPage from '@Pages/Register.page'
import AppHeader from '@Components/AppHeader'
import AppFooter from '@Components/AppFooter'
import AuthenticationOnly from '@Components/AuthenticatedOnly'
import ProfilePages from './pages/Profile.pages'
import DetailsPage from './pages/Details.page'

import '@Styles/App.css'

function App() {
	return (
		<div className='App'>
			<AppHeader />
			<Routes>
				<Route path='/sign-up' element={<RegisterPage />} />
				<Route path='/sign-in' element={<LoginPage />} />

				<Route path='/profile/*' element={<AuthenticationOnly><ProfilePages /></AuthenticationOnly>} />
				<Route path='/upload/*' element={<AuthenticationOnly><CreatePages /></AuthenticationOnly>} />
			

				<Route path='/details/:id' element={<DetailsPage />} />

			</Routes>
		
			<AppFooter />
		</div>
	)
}

export default App
