import React from 'react'
import { Route, Routes } from 'react-router'

import UpdateProfilepage from '@Pages/UpdateProfile.page'
import LoginPage from '@Pages/login.page'
import CreatePages from '@Pages/upload.pages'
import RegisterPage from '@Pages/Register.page'
import Profilepage from '@Pages/Profile.page'
import AppHeader from '@Components/AppHeader'
import AppFooter from '@Components/AppFooter'
import AuthenticationOnly from '@Components/AuthenticatedOnly'

import '@Styles/App.css'

function App() {
	return (
		<div className='App'>
			<AppHeader />
			<Routes>
				<Route path='/sign-up' element={<RegisterPage />} />
				<Route path='/sign-in' element={<LoginPage />} />

				<Route path='/updateprofile' element={<AuthenticationOnly><UpdateProfilepage /> </AuthenticationOnly>} />
				
			    <Route path='/profile' element={<Profilepage/>}/>

				<Route path='/upload/*' element={<AuthenticationOnly><CreatePages /></AuthenticationOnly>} />
			</Routes>
		
			<AppFooter />
		</div>
	)
}

export default App
