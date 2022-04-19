import React from "react";
import LoginForm from "../components/LoginForm";
import { Link } from "react-router-dom";


import '../styles/forms.css'
import '../styles/LoginPage.css'

const LoginPage = () => {
    return (
        <div className='Login center-container page'>
           
                <div className='container card'>
                    <h2>  Login  </h2>
                    <LoginForm />
                    <div className='Signup'>
                    <h6> Don't have an account ? <Link className='' to='/sign-up'>Sign UP</Link></h6> 
                    <h6> Forgot  <Link className='' to='#'>Password</Link> ?</h6>
                    </div>
                </div>
               
        </div>

    )

}
export default LoginPage;