(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('LegalEntityFormDialogController', LegalEntityFormDialogController);

    LegalEntityFormDialogController.$inject = ['$scope', '$stateParams', 'DataUtils', 'entity', 'LegalEntityForm', 'Gender', '$translate'];

    function LegalEntityFormDialogController($scope, $stateParams, DataUtils, entity, LegalEntityForm, Gender, $translate) {
        var vm = this;
        vm.legalEntityForm = entity;
        // vm.genders = Gender.query();
        vm.load = function (id) {
            LegalEntityForm.get({id: id}, function (result) {
                vm.legalEntityForm = result;
            });
        };

        var onSaveSuccess = function (result) {
            // $scope.$emit('volunteercrowdApp:legalEntityFormUpdate', result);
            vm.success = 'OK';
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function (response) {
            vm.isSaving = false;
            vm.success = null;
            if (response.status === 400 && response.data === 'login already in use') {
                vm.errorUserExists = 'ERROR';
            } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                vm.errorEmailExists = 'ERROR';
            } else {
                vm.error = 'ERROR';
            }
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.legalEntityForm.password !== vm.legalEntityForm.passwordConfirm) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.legalEntityForm.langKey = $translate.use();
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                LegalEntityForm.save(vm.legalEntityForm, function (result) {
                    onSaveSuccess(result);
                }, function (response) {
                    onSaveError(response);
                });
            }


        };

        vm.clear = function () {
            //$uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.birthDate = false;

        vm.setImage = function ($file, legalEntityForm) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        legalEntityForm.image = base64Data;
                        legalEntityForm.imageContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function (date) {
            var _date = new Date();
            var day = _date.getDate();
            var month = _date.getMonth();
            var year = _date.getFullYear();
            var hour = _date.getHours();
            var min = _date.getMinutes();

            //Formatting the numbers when below 10
            if (month < 10) {
                month = "0" + month;
            }
            if (day < 10) {
                day = "0" + day;
            }
            if (hour < 10) {
                hour = "0" + hour;
            }
            if (min < 10) {
                min = "0" + min;
            }

            var now_date = year + "-" + month + "-" + day + " " + hour + ":" + min;

            if ($("#field_birthDate").val()) {
                _date = $("#field_birthDate").val();
            } else {
                _date = now_date;
            }

            $("#field_birthDate").val(_date).change();
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
