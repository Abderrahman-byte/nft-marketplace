import React from 'react'
import { Route, Routes } from 'react-router'

import AppFooter from './components/AppFooter'
import AppHeader from './components/AppHeader'
import RegisterPage from './pages/Register.page'
import LoginPage from './pages/login.page'
import UpdateProfilepage from './pages/UpdateProfile.page'
import AuthenticationOnly from './components/AuthenticatedOnly'
import CreatePages from './pages/upload.pages'
import Profilepage from './pages/Profile.page'


import './styles/App.css'

function App() {
	return (
		<div className='App'>
			<AppHeader />
			<Routes>
				<Route path='/sign-up' element={<RegisterPage />} />
				<Route path='/sign-in' element={<LoginPage />} />
				<Route path='/updateprofile' element={<AuthenticationOnly><UpdateProfilepage /> </AuthenticationOnly>} />
				<Route path='create' element={<AuthenticationOnly><CreatePages /></AuthenticationOnly>} />
			    <Route path='/profile' element={<Profilepage/>}/>
			</Routes>
		
			<AppFooter />
		</div>
	)
}

export default App
