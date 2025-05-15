package com.example.mediareelzsv3v2.Objects

class ModifyUserButtonStateManager {


    companion object{
        private var modifyUserUpdateUserButtonState: Boolean = true
        private var modifyUserDeleteUserButtonState: Boolean = true
        private var modifyUserBackButtonButtonState: Boolean = true


        fun getModifyUserUpdateUserButtonState():Boolean{

            return  modifyUserUpdateUserButtonState

        }

        fun setModifyUserUpdateUserButtonState(modifyUserUpdateUserButtonState:Boolean){
            Companion.modifyUserUpdateUserButtonState = modifyUserUpdateUserButtonState

        }

        fun getModifyUserDeleteUserButtonState():Boolean{

            return  modifyUserDeleteUserButtonState

        }

        fun setModifyUserDeleteUserButtonState( modifyUserDeleteUserButtonState:Boolean){

            Companion.modifyUserDeleteUserButtonState = modifyUserDeleteUserButtonState

        }

        fun  getModifyUserBackButtonButtonState():Boolean{

            return  modifyUserBackButtonButtonState

        }

        fun  setModifyUserBackButtonButtonState(modifyUserBackButtonButtonState:Boolean){

            Companion.modifyUserBackButtonButtonState = modifyUserBackButtonButtonState

        }











    }




}