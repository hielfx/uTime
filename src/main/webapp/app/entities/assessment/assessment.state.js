(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('assessment', {
            parent: 'entity',
            url: '/assessment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.assessment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/assessment/assessments.html',
                    controller: 'AssessmentController',
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
                    $translatePartialLoader.addPart('assessment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('assessment-detail', {
            parent: 'entity',
            url: '/assessment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.assessment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/assessment/assessment-detail.html',
                    controller: 'AssessmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('assessment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Assessment', function($stateParams, Assessment) {
                    return Assessment.get({id : $stateParams.id});
                }]
            }
        })
        .state('assessment.new', {
            parent: 'assessment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assessment/assessment-dialog.html',
                    controller: 'AssessmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationMoment: null,
                                rating: null,
                                comment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('assessment', null, { reload: true });
                }, function() {
                    $state.go('assessment');
                });
            }]
        })
        .state('assessment.edit', {
            parent: 'assessment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assessment/assessment-dialog.html',
                    controller: 'AssessmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Assessment', function(Assessment) {
                            return Assessment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('assessment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('assessment.delete', {
            parent: 'assessment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/assessment/assessment-delete-dialog.html',
                    controller: 'AssessmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Assessment', function(Assessment) {
                            return Assessment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('assessment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
