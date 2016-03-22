'use strict';

angular.module('volunteercrowdApp')
    .controller('NaturalPersonController', function ($scope, $state, NaturalPerson, NaturalPersonSearch, ParseLinks) {

        $scope.naturalPersons = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            NaturalPerson.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.naturalPersons = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            NaturalPersonSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.naturalPersons = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.naturalPerson = {
                birthDate: null,
                id: null
            };
        };
    });
