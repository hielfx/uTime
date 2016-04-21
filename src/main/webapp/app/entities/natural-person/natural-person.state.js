(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('natural-person', {
            parent: 'entity',
            url: '/natural-person?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.naturalPerson.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/natural-person/natural-people.html',
                    controller: 'NaturalPersonController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('naturalPerson');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('natural-person-detail', {
            parent: 'entity',
            url: '/natural-person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.naturalPerson.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/natural-person/natural-person-detail.html',
                    controller: 'NaturalPersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('naturalPerson');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NaturalPerson', function($stateParams, NaturalPerson) {
                    return NaturalPerson.get({id : $stateParams.id});
                }]
            }
        })
        .state('natural-person.new', {
            parent: 'natural-person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/natural-person/natural-person-dialog.html',
                    controller: 'NaturalPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                birthDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('natural-person', null, { reload: true });
                }, function() {
                    $state.go('natural-person');
                });
            }]
        })
        .state('natural-person.edit', {
            parent: 'natural-person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/natural-person/natural-person-dialog.html',
                    controller: 'NaturalPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NaturalPerson', function(NaturalPerson) {
                            return NaturalPerson.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('natural-person', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('natural-person.delete', {
            parent: 'natural-person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/natural-person/natural-person-delete-dialog.html',
                    controller: 'NaturalPersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NaturalPerson', function(NaturalPerson) {
                            return NaturalPerson.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('natural-person', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
