(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NaturalPersonFormController', NaturalPersonFormController);

    NaturalPersonFormController.$inject = ['$scope', '$state', 'DataUtils', 'NaturalPersonForm', 'NaturalPersonFormSearch'];

    function NaturalPersonFormController($scope, $state, DataUtils, NaturalPersonForm, NaturalPersonFormSearch) {
        var vm = this;
        vm.naturalPersonForms = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.loadAll = function () {
            NaturalPersonForm.query(function (result) {
                vm.naturalPersonForms = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NaturalPersonFormSearch.query({query: vm.searchQuery}, function (result) {
                vm.naturalPersonForms = result;
            });
        };
        vm.loadAll();

    }
})();
